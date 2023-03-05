package cloud.terium.plugin.impl.pipe;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.group.*;
import cloud.terium.networking.packet.node.PacketPlayOutNodeAdd;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdowned;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.networking.packet.player.*;
import cloud.terium.networking.packet.service.*;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateAdd;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.impl.node.Node;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.events.group.CloudGroupCreatedEvent;
import cloud.terium.teriumapi.events.group.CloudGroupDeleteEvent;
import cloud.terium.teriumapi.events.group.CloudGroupUpdatedEvent;
import cloud.terium.teriumapi.events.node.NodeLoggedInEvent;
import cloud.terium.teriumapi.events.node.NodeLoggedOutEvent;
import cloud.terium.teriumapi.events.player.*;
import cloud.terium.teriumapi.events.service.CloudServiceStartedEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStoppedEvent;
import cloud.terium.teriumapi.events.service.CloudServiceUpdateEvent;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.impl.CloudPlayer;
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

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TeriumNetworking implements IDefaultTeriumNetworking {

    private final TeriumClient teriumClient;

    @SneakyThrows
    public TeriumNetworking() {
        teriumClient = new TeriumClient(System.getProperty("netty-address"), Integer.parseInt(System.getProperty("netty-port")));
        getChannel().pipeline().addLast(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet) {
                // Nodes
                try {
                    if (packet instanceof PacketPlayOutNodeAdd newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getAllNodes().add(new Node(newPacket.name(), newPacket.key(), newPacket.address(), newPacket.memory(), newPacket.connected()));

                    // Templates
                    if (packet instanceof PacketPlayOutTemplateAdd newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getAllTemplates().add(new Template(newPacket.name(), Path.of(newPacket.path())));

                    // Groups
                    if (packet instanceof PacketPlayOutGroupAdd newPacket) {
                        List<INode> fallbackNodes = new ArrayList<>();
                        List<ITemplate> templates = new ArrayList<>();
                        newPacket.fallbackNodes().forEach(s -> fallbackNodes.add(TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(s).orElseGet(null)));
                        newPacket.templates().forEach(s -> templates.add(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(s).orElseGet(null)));

                        switch (newPacket.serviceType()) {
                            case Lobby ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultLobbyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), fallbackNodes, templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Server ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultServerGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), fallbackNodes, templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Proxy ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultProxyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), fallbackNodes, templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.port(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                        }
                    }

                    // Services
                    if (packet instanceof PacketPlayOutServiceAdd newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllCloudServices().add(new CloudService(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.parsedNode().orElseGet(null), newPacket.parsedServiceGroup().orElseGet(null), newPacket.parsedTemplates()));
                    if (packet instanceof PacketPlayOutServiceRemove newPacket)
                        newPacket.parsedCloudService().ifPresent(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllCloudServices().remove(cloudService));

                    if (packet instanceof PacketPlayOutUpdateService newPacket) {
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(newPacket.serviceName()).ifPresent(cloudService -> {
                            cloudService.setUsedMemory((long) newPacket.memory());
                            cloudService.setServiceState(newPacket.serviceState());
                            cloudService.setOnlinePlayers(newPacket.players());
                            cloudService.setLocked(newPacket.locked());
                        });
                    }

                    // Players
                    if (packet instanceof PacketPlayOutCloudPlayerAdd newPacket) {
                        if (newPacket.online())
                            TeriumPlugin.getInstance().getCloudPlayerProvider().getOnlinePlayers().add(new CloudPlayer(newPacket.username(), newPacket.uniquedId(), newPacket.address().getHostName(), newPacket.parsedCloudService()));
                        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getRegisteredPlayers().add(new CloudPlayer(newPacket.username(), newPacket.uniquedId(), newPacket.address().getHostName(), newPacket.parsedCloudService()));
                    }
                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket) {
                        ICloudPlayer cloudPlayer = TeriumPlugin.getInstance().getCloudPlayerProvider().getCloudPlayer(newPacket.uniquedId()).orElseGet(null);
                        cloudPlayer.updateUsername(newPacket.username());
                        cloudPlayer.updateAddress(newPacket.address());
                        cloudPlayer.updateConnectedService(newPacket.parsedCloudService().orElseGet(null));
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
                    if (packet instanceof PacketPlayOutGroupUpdate newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudGroupUpdatedEvent(newPacket.parsedServiceGroup().orElseGet(null)));
                    // node
                    if (packet instanceof PacketPlayOutNodeStarted newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new NodeLoggedInEvent(newPacket.parsedNode().orElseGet(null)));
                    if (packet instanceof PacketPlayOutNodeShutdowned newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new NodeLoggedOutEvent(newPacket.parsedNode().orElseGet(null)));
                    // player
                    if (packet instanceof PacketPlayOutCloudPlayerJoin newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerJoinEvent(newPacket.parsedCloudPlayer().orElseGet(null)));
                    if (packet instanceof PacketPlayOutCloudPlayerQuit newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerQuitEvent(newPacket.parsedCloudPlayer().orElseGet(null)));
                    if (packet instanceof PacketPlayOutCloudPlayerConnectedService newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerServiceConnectedEvent(newPacket.parsedCloudPlayer().orElseGet(null), newPacket.parsedCloudService().orElseGet(null)));
                    if (packet instanceof PacketPlayOutCloudPlayerConnect newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerServiceConnectEvent(newPacket.parsedCloudPlayer().orElseGet(null), newPacket.parsedCloudService().orElseGet(null)));
                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudPlayerUpdateEvent(newPacket.parsedCloudPlayer().orElseGet(null), newPacket.username(), newPacket.address(), newPacket.parsedCloudService().orElseGet(null)));
                    // service
                    if (packet instanceof PacketPlayOutSuccessfullyServiceStarted newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartedEvent(newPacket.parsedCloudService().orElseGet(null)));
                    if (packet instanceof PacketPlayOutServiceAdd newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartingEvent(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(newPacket.serviceName()).orElseGet(null)));
                    if (packet instanceof PacketPlayOutServiceRemove newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStoppedEvent(newPacket.parsedCloudService().orElseGet(null)));
                    if (packet instanceof PacketPlayOutUpdateService newPacket)
                        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceUpdateEvent(newPacket.parsedCloudService().orElseGet(null), newPacket.locked(), newPacket.serviceState(), newPacket.players(), (long) newPacket.memory()));

                    if (TeriumAPI.getTeriumAPI().getProvider().getThisService() != null) {
                        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceType().equals(ServiceType.Proxy)) {
                            if (packet instanceof PacketPlayOutSuccessfullyServiceStarted packetAdd) {
                                if (!TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(packetAdd.serviceName()).orElseGet(null).getServiceType().equals(ServiceType.Proxy)) {
                                    if (TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetAdd.serviceName()).isPresent()) {
                                        return;
                                    }

                                    TeriumVelocityStartup.getInstance().getProxyServer().registerServer(new ServerInfo(packetAdd.serviceName(), new InetSocketAddress("127.0.0.1", packetAdd.parsedCloudService().orElseGet(null).getPort())));
                                }
                            }

                            if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                                TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(packetRemove.serviceName()).ifPresentOrElse(cloudService -> {
                                    TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetRemove.serviceName()).ifPresent(registeredServer -> TeriumVelocityStartup.getInstance().getProxyServer().unregisterServer(registeredServer.getServerInfo()));
                                }, () -> {
                                    System.out.println("This service isn't connected with the proxy service.");
                                });
                            }

                            if (packet instanceof PacketPlayOutCloudPlayerConnect packetConnect) {
                                TeriumVelocityStartup.getInstance().getProxyServer().getPlayer(packetConnect.cloudPlayer()).ifPresent(player -> player.createConnectionRequest(TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetConnect.cloudService()).orElse(null)).connect());
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
        sendPacket(new PacketPlayOutServiceRegister());
    }

    @Override
    public Channel getChannel() {
        return teriumClient.getChannel();
    }

    public void addHandler(SimpleChannelInboundHandler<Packet> handler) {
        getChannel().pipeline().addLast(handler);
    }

    @Override
    public void sendPacket(Packet packet) {
        getChannel().writeAndFlush(packet);
    }
}
