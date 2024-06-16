package cloud.terium.extension.impl.pipe;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.extension.impl.module.LoadedModule;
import cloud.terium.extension.impl.node.Node;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.networking.packet.group.*;
import cloud.terium.networking.packet.module.PacketPlayOutAddLoadedModule;
import cloud.terium.networking.packet.node.*;
import cloud.terium.networking.packet.player.*;
import cloud.terium.networking.packet.service.*;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateAdd;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.impl.CloudPlayer;
import cloud.terium.teriumapi.events.config.ReloadConfigEvent;
import cloud.terium.teriumapi.events.group.*;
import cloud.terium.teriumapi.events.node.*;
import cloud.terium.teriumapi.events.player.*;
import cloud.terium.teriumapi.events.service.*;
import cloud.terium.teriumapi.pipe.Handler;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.*;
import cloud.terium.teriumapi.service.group.impl.*;
import cloud.terium.teriumapi.service.impl.CloudService;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.impl.Template;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeriumNetworking implements IDefaultTeriumNetworking {

    private final TeriumClient teriumClient;
    private final List<Handler> handlers;

    public TeriumNetworking() {
        teriumClient = new TeriumClient(System.getProperty("netty-address"), Integer.parseInt(System.getProperty("netty-port")));
        this.handlers = new ArrayList<>();
        getChannel().pipeline().addLast(new SimpleChannelInboundHandler<>() {
            @SneakyThrows
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet) {
                // Nodes
                try {
                    for (Handler handler : handlers) {
                        Method method = handler.getClass().getDeclaredMethod("onReceive", Object.class);

                        try {
                            method.invoke(handler, packet);
                        } catch (IllegalAccessException | InvocationTargetException exception) {
                            exception.printStackTrace();
                        }
                    }

                    if (packet instanceof PacketPlayOutNodeAdd newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getAllNodes().add(new Node(newPacket.name(), newPacket.address(), newPacket.memory(), newPacket.connected()));

                    // Templates
                    if (packet instanceof PacketPlayOutTemplateAdd newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getAllTemplates().add(new Template(newPacket.name(), Path.of(newPacket.path())));

                    // Groups
                    if (packet instanceof PacketPlayOutGroupAdd newPacket) {
                        List<ITemplate> templates = new ArrayList<>();
                        newPacket.templates().forEach(s -> templates.add(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(s).orElseThrow()));

                        switch (newPacket.serviceType()) {
                            case Lobby ->
                                    TeriumExtension.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultLobbyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseThrow(), templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Server ->
                                    TeriumExtension.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultServerGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseThrow(), templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Proxy ->
                                    TeriumExtension.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultProxyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseThrow(), templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.port(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                        }
                    }

                    // Services
                    if (packet instanceof PacketPlayOutServiceAdd newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().add(new CloudService(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.memory(), newPacket.parsedNode().orElseThrow(), newPacket.parsedServiceGroup().orElseThrow(), newPacket.parsedTemplates(), newPacket.propertyCache()));
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartingEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(newPacket.serviceName()).orElseThrow()));
                    }
                    if (packet instanceof PacketPlayOutServiceRemove newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(newPacket.serviceName()).ifPresentOrElse(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStoppedEvent(newPacket.parsedCloudService().orElseThrow())),
                                () -> System.out.println("Service with that name isn't registered!"));
                        newPacket.parsedCloudService().ifPresent(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().remove(cloudService));
                    }
                    if (packet instanceof PacketPlayOutUpdateService newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(newPacket.serviceName()).ifPresent(cloudService -> {
                            cloudService.setUsedMemory((long) newPacket.memory());
                            cloudService.setServiceState(newPacket.serviceState());
                            cloudService.setOnlinePlayers(newPacket.players());
                            cloudService.setLocked(newPacket.locked());
                            cloudService.getPropertyMap().clear();
                            newPacket.propertyCache().forEach(cloudService::addProperty);
                        });

                    if (packet instanceof PacketPlayOutServiceAddProperties newPacket) {
                        newPacket.propertiesCache().forEach((s, o) -> TeriumAPI.getTeriumAPI().getProvider().getThisService().addProperty(s, o));
                        TeriumAPI.getTeriumAPI().getProvider().getThisService().update();
                    }

                    // Players
                    if (packet instanceof PacketPlayOutCloudPlayerAdd newPacket)
                        TeriumExtension.getInstance().getCloudPlayerProvider().getOnlinePlayers().add(new CloudPlayer(newPacket.username(), newPacket.uniquedId(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.parsedCloudService()));

                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket) {
                        TeriumExtension.getInstance().getCloudPlayerProvider().getCloudPlayer(newPacket.uniquedId()).ifPresent(cloudPlayer -> {
                            cloudPlayer.updateUsername(newPacket.username());
                            cloudPlayer.updateAddress(newPacket.address());
                            cloudPlayer.updateSkinData(newPacket.value(), newPacket.signature());
                            cloudPlayer.updateConnectedService(newPacket.parsedCloudService().orElseThrow());
                        });
                    }

                    // module
                    if (packet instanceof PacketPlayOutAddLoadedModule newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().add(new LoadedModule(newPacket.name(),
                                newPacket.fileName(), newPacket.author(), newPacket.version(), newPacket.description(), newPacket.mainClass(), newPacket.reloadable(), newPacket.moduleType()));

                    // Event casting and group
                    if (packet instanceof PacketPlayOutCreateServerGroup newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupCreatedEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(newPacket.name()).orElseThrow()));
                    if (packet instanceof PacketPlayOutCreateLobbyGroup newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupCreatedEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(newPacket.name()).orElseThrow()));
                    if (packet instanceof PacketPlayOutCreateProxyGroup newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupCreatedEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(newPacket.name()).orElseThrow()));
                    if (packet instanceof PacketPlayOutGroupDelete newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupDeleteEvent(newPacket.parsedServiceGroup().orElseThrow()));
                    if (packet instanceof PacketPlayOutGroupUpdate newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupUpdatedEvent(newPacket.parsedServiceGroup().orElseThrow()));
                        newPacket.parsedServiceGroup().ifPresent(serviceGroup -> {
                            serviceGroup.setGroupNode(TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseThrow());
                            serviceGroup.setMemory(newPacket.memory());
                            serviceGroup.setVersion(newPacket.version());
                            serviceGroup.setStatic(newPacket.isStatic());
                            serviceGroup.setMaintenance(newPacket.maintenance());
                            serviceGroup.setMaxPlayer(newPacket.maximumPlayers());
                            serviceGroup.setMinServices(newPacket.minimalServices());
                            serviceGroup.setMaxServices(newPacket.maximalServices());
                        });
                    }
                    // node
                    if (packet instanceof PacketPlayOutNodeStarted newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new NodeLoggedInEvent(newPacket.parsedNode().orElseThrow()));
                    if (packet instanceof PacketPlayOutNodeShutdowned newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new NodeLoggedOutEvent(newPacket.parsedNode().orElseThrow()));
                    // player
                    if (packet instanceof PacketPlayOutCloudPlayerJoin newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerJoinEvent(cloudPlayer)));
                    if (packet instanceof PacketPlayOutCloudPlayerQuit newPacket) {
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> {
                            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerQuitEvent(cloudPlayer));
                            TeriumExtension.getInstance().getCloudPlayerProvider().getOnlinePlayers().remove(cloudPlayer);
                        });
                    }
                    if (packet instanceof PacketPlayOutCloudPlayerConnectedService newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerServiceConnectedEvent(cloudPlayer, newPacket.parsedCloudService().orElseThrow())));
                    if (packet instanceof PacketPlayOutCloudPlayerConnect newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerServiceConnectEvent(cloudPlayer, newPacket.parsedCloudService().orElseThrow())));
                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerUpdateEvent(cloudPlayer, newPacket.username(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.parsedCloudService().orElseThrow())));
                    // service
                    if (packet instanceof PacketPlayOutSuccessfullyServiceStarted newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartedEvent(newPacket.parsedCloudService().orElseThrow()));
                    if (packet instanceof PacketPlayOutUpdateService newPacket)
                        newPacket.parsedCloudService().ifPresent(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceUpdateEvent(cloudService, newPacket.locked(), newPacket.serviceState(), newPacket.players(), (long) newPacket.memory())));

                    if (TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(TeriumExtension.getInstance().getThisName()).isPresent()) {
                        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceType().equals(ServiceType.Proxy)) {
                            if (packet instanceof PacketPlayOutSuccessfullyServiceStarted packetAdd) {
                                Optional<ICloudService> serviceOpt = TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(packetAdd.serviceName());
                                ICloudService service = serviceOpt.orElse(null);

                                if (service != null && !service.getServiceType().equals(ServiceType.Proxy)) {
                                    if (TeriumExtension.getInstance().checkServerIsRegistered(packetAdd.serviceName()))
                                        return;

                                    InetSocketAddress address = new InetSocketAddress(
                                            packetAdd.parsedNode().orElse(null).getAddress().getAddress().getHostAddress(),
                                            packetAdd.parsedCloudService().orElse(null).getPort());

                                    TeriumExtension.getInstance().registerServer(service, address);
                                }
                            }

                            if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                                if (TeriumExtension.getInstance().checkServerIsRegistered(packetRemove.serviceName())) {
                                    packetRemove.parsedCloudService().ifPresent(service -> TeriumExtension.getInstance().unregisterServer(service));
                                    return;
                                }

                                System.out.println("This server isn't registered! (" + packetRemove.serviceName() + ")");
                            }

                            if (packet instanceof PacketPlayOutCloudPlayerDisconnect disconnect) {
                                TeriumExtension.getInstance().disconnectPlayer(disconnect.cloudPlayer(), disconnect.message());
                            }

                            if (packet instanceof PacketPlayOutCloudPlayerConnect packetConnect)
                                packetConnect.parsedCloudService().ifPresent(cloudService -> TeriumExtension.getInstance().connectPlayer(packetConnect.cloudPlayer(), cloudService));

                            if (packet instanceof PacketPlayOutServiceExecuteCommand newPacket)
                                if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName().equals(newPacket.cloudService()))
                                    TeriumExtension.getInstance().executeCommand(newPacket.command());
                        } else {
                            if (packet instanceof PacketPlayOutServiceExecuteCommand newPacket)
                                if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName().equals(newPacket.cloudService()))
                                    TeriumExtension.getInstance().executeCommand(newPacket.command());
                        }
                    }

                    // reload
                    if (packet instanceof PacketPlayOutReloadConfig)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new ReloadConfigEvent());
                    if (packet instanceof PacketPlayOutGroupsReload)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupsReloadEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups()));
                } catch (
                        Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        sendPacket(new PacketPlayOutServiceRegister());
    }

    @Override
    public String getHostAddress() {
        return System.getProperty("netty-address");
    }

    @Override
    public int getPort() {
        return Integer.parseInt(System.getProperty("netty-port"));
    }

    @Override
    public Channel getChannel() {
        return teriumClient.getChannel();
    }

    @Override
    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    @Override
    public void sendPacket(Packet packet) {
        getChannel().writeAndFlush(packet);
    }
}
