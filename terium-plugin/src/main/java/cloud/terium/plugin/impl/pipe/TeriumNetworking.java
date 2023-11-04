package cloud.terium.plugin.impl.pipe;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.networking.packet.group.*;
import cloud.terium.networking.packet.module.PacketPlayOutAddLoadedModule;
import cloud.terium.networking.packet.node.PacketPlayOutNodeAdd;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdowned;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.networking.packet.player.*;
import cloud.terium.networking.packet.service.*;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateAdd;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.bukkit.TeriumBukkitStartup;
import cloud.terium.plugin.impl.module.LoadedModule;
import cloud.terium.plugin.impl.node.Node;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.impl.CloudPlayer;
import cloud.terium.teriumapi.events.config.ReloadConfigEvent;
import cloud.terium.teriumapi.events.group.CloudGroupCreatedEvent;
import cloud.terium.teriumapi.events.group.CloudGroupDeleteEvent;
import cloud.terium.teriumapi.events.group.CloudGroupUpdatedEvent;
import cloud.terium.teriumapi.events.group.CloudGroupsReloadEvent;
import cloud.terium.teriumapi.events.node.NodeLoggedInEvent;
import cloud.terium.teriumapi.events.node.NodeLoggedOutEvent;
import cloud.terium.teriumapi.events.player.*;
import cloud.terium.teriumapi.events.service.CloudServiceStartedEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStoppedEvent;
import cloud.terium.teriumapi.events.service.CloudServiceUpdateEvent;
import cloud.terium.teriumapi.pipe.Handler;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import cloud.terium.teriumapi.service.impl.CloudService;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.impl.Template;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
                        newPacket.templates().forEach(s -> templates.add(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(s).orElseGet(null)));

                        switch (newPacket.serviceType()) {
                            case Lobby ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultLobbyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Server ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultServerGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Proxy ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultProxyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.port(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                        }
                    }

                    // Services
                    if (packet instanceof PacketPlayOutServiceAdd newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().add(new CloudService(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.memory(), newPacket.parsedNode().orElseGet(null), newPacket.parsedServiceGroup().orElseGet(null), newPacket.parsedTemplates(), newPacket.propertyCache()));
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartingEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(newPacket.serviceName()).orElseGet(null)));
                    }
                    if (packet instanceof PacketPlayOutServiceRemove newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(newPacket.serviceName()).ifPresentOrElse(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStoppedEvent(newPacket.parsedCloudService().orElseGet(null))), () -> {
                            System.out.println("Service with that name isn't registered!");
                        });
                        newPacket.parsedCloudService().ifPresent(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().remove(cloudService));
                    }
                    if (packet instanceof PacketPlayOutUpdateService newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(newPacket.serviceName()).ifPresent(cloudService -> {
                            cloudService.setUsedMemory((long) newPacket.memory());
                            cloudService.setServiceState(newPacket.serviceState());
                            cloudService.setOnlinePlayers(newPacket.players());
                            cloudService.setLocked(newPacket.locked());
                            cloudService.getPropertyMap().clear();
                            newPacket.propertyCache().forEach(cloudService::addProperty);
                        });
                    }

                    if (packet instanceof PacketPlayOutServiceAddProperties newPacket) {
                        newPacket.propertiesCache().forEach((s, o) -> TeriumAPI.getTeriumAPI().getProvider().getThisService().addProperty(s, o));
                        TeriumAPI.getTeriumAPI().getProvider().getThisService().update();
                    }

                    // Players
                    if (packet instanceof PacketPlayOutCloudPlayerAdd newPacket) {
                        TeriumPlugin.getInstance().getCloudPlayerProvider().getOnlinePlayers().add(new CloudPlayer(newPacket.username(), newPacket.uniquedId(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.parsedCloudService()));
                    }

                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket) {
                        TeriumPlugin.getInstance().getCloudPlayerProvider().getCloudPlayer(newPacket.uniquedId()).ifPresent(cloudPlayer -> {
                            cloudPlayer.updateUsername(newPacket.username());
                            cloudPlayer.updateAddress(newPacket.address());
                            cloudPlayer.updateSkinData(newPacket.value(), newPacket.signature());
                            cloudPlayer.updateConnectedService(newPacket.parsedCloudService().orElseGet(null));
                        });
                    }

                    // module
                    if (packet instanceof PacketPlayOutAddLoadedModule newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().add(new LoadedModule(newPacket.name()
                                , newPacket.fileName(), newPacket.author(), newPacket.version(), newPacket.description(), newPacket.mainClass(), newPacket.reloadable(), newPacket.moduleType()));
                    }

                    // Event cast
                    // group
                    if (packet instanceof PacketPlayOutCreateServerGroup newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupCreatedEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(newPacket.name()).orElseGet(null)));
                    if (packet instanceof PacketPlayOutCreateLobbyGroup newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupCreatedEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(newPacket.name()).orElseGet(null)));
                    if (packet instanceof PacketPlayOutCreateProxyGroup newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupCreatedEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(newPacket.name()).orElseGet(null)));
                    if (packet instanceof PacketPlayOutGroupDelete newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupDeleteEvent(newPacket.parsedServiceGroup().orElseGet(null)));
                    if (packet instanceof PacketPlayOutGroupUpdate newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupUpdatedEvent(newPacket.parsedServiceGroup().orElseGet(null)));
                        newPacket.parsedServiceGroup().ifPresent(serviceGroup -> {
                            serviceGroup.setGroupNode(TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null));
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
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new NodeLoggedInEvent(newPacket.parsedNode().orElseGet(null)));
                    if (packet instanceof PacketPlayOutNodeShutdowned newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new NodeLoggedOutEvent(newPacket.parsedNode().orElseGet(null)));
                    // player
                    if (packet instanceof PacketPlayOutCloudPlayerJoin newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> {
                            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerJoinEvent(cloudPlayer));
                        });
                    if (packet instanceof PacketPlayOutCloudPlayerQuit newPacket) {
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> {
                            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerQuitEvent(cloudPlayer));
                            TeriumPlugin.getInstance().getCloudPlayerProvider().getOnlinePlayers().remove(cloudPlayer);
                        });
                    }
                    if (packet instanceof PacketPlayOutCloudPlayerConnectedService newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> {
                            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerServiceConnectedEvent(cloudPlayer, newPacket.parsedCloudService().orElseGet(null)));
                        });
                    if (packet instanceof PacketPlayOutCloudPlayerConnect newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> {
                            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerServiceConnectEvent(cloudPlayer, newPacket.parsedCloudService().orElseGet(null)));
                        });
                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket)
                        newPacket.parsedCloudPlayer().ifPresent(cloudPlayer -> {
                            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerUpdateEvent(cloudPlayer, newPacket.username(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.parsedCloudService().orElseGet(null)));
                        });
                    // service
                    if (packet instanceof PacketPlayOutSuccessfullyServiceStarted newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartedEvent(newPacket.parsedCloudService().orElseGet(null)));
                    if (packet instanceof PacketPlayOutUpdateService newPacket)
                        newPacket.parsedCloudService().ifPresent(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceUpdateEvent(cloudService, newPacket.locked(), newPacket.serviceState(), newPacket.players(), (long) newPacket.memory())));

                    if (TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(TeriumPlugin.getInstance().getThisName()).isPresent()) {
                        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceType().equals(ServiceType.Proxy)) {
                            if (packet instanceof PacketPlayOutSuccessfullyServiceStarted packetAdd) {
                                if (!TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(packetAdd.serviceName()).orElseGet(null).getServiceType().equals(ServiceType.Proxy)) {
                                    if (TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetAdd.serviceName()).isPresent()) {
                                        return;
                                    }

                                    TeriumVelocityStartup.getInstance().getProxyServer().registerServer(new ServerInfo(packetAdd.serviceName(), new InetSocketAddress(packetAdd.parsedNode().orElseGet(null).getAddress().getAddress().getHostAddress(), packetAdd.parsedCloudService().orElseGet(null).getPort())));
                                }
                            }

                            if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                                TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetRemove.serviceName()).ifPresentOrElse(registeredServer -> TeriumVelocityStartup.getInstance().getProxyServer().unregisterServer(registeredServer.getServerInfo()), () -> {
                                    System.out.println("This server isn't registered! (" + packetRemove.serviceName() + ")");
                                });
                            }

                            if (packet instanceof PacketPlayOutCloudPlayerDisconnect disconnect) {
                                TeriumVelocityStartup.getInstance().getProxyServer().getPlayer(disconnect.cloudPlayer()).ifPresent(player -> player.disconnect(disconnect.message().contains("§") ? Component.text(disconnect.message()) : MiniMessage.miniMessage().deserialize(disconnect.message())));
                            }

                            if (packet instanceof PacketPlayOutCloudPlayerConnect packetConnect) {
                                TeriumVelocityStartup.getInstance().getProxyServer().getPlayer(packetConnect.cloudPlayer()).ifPresent(player -> player.createConnectionRequest(TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetConnect.cloudService()).orElse(null)).connect());
                            }

                            if (packet instanceof PacketPlayOutServiceExecuteCommand newPacket)
                                if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName().equals(newPacket.cloudService()))
                                    TeriumVelocityStartup.getInstance().executeCommand(newPacket.command());
                        } else {
                            if (packet instanceof PacketPlayOutServiceExecuteCommand newPacket)
                                if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName().equals(newPacket.cloudService()))
                                    TeriumBukkitStartup.getInstance().executeCommand(newPacket.command());
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
