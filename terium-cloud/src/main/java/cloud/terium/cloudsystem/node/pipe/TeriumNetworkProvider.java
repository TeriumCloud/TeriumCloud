package cloud.terium.cloudsystem.node.pipe;

import cloud.terium.cloudsystem.common.event.events.console.RegisterCommandEvent;
import cloud.terium.cloudsystem.common.event.events.console.SendConsoleEvent;
import cloud.terium.cloudsystem.common.event.events.group.*;
import cloud.terium.cloudsystem.common.event.events.node.NodeLoggedInEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownedEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeUpdateEvent;
import cloud.terium.cloudsystem.common.event.events.player.CloudPlayerConnectEvent;
import cloud.terium.cloudsystem.common.event.events.player.CloudPlayerConnectedToServiceEvent;
import cloud.terium.cloudsystem.common.event.events.player.CloudPlayerJoinEvent;
import cloud.terium.cloudsystem.common.event.events.player.CloudPlayerQuitEvent;
import cloud.terium.cloudsystem.common.event.events.service.*;
import cloud.terium.cloudsystem.common.event.events.service.template.TemplateCreateEvent;
import cloud.terium.cloudsystem.common.event.events.service.template.TemplateDeleteEvent;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.console.PacketPlayOutRegisterCommand;
import cloud.terium.networking.packet.console.PacketPlayOutSendConsole;
import cloud.terium.networking.packet.group.*;
import cloud.terium.networking.packet.module.PacketPlayOutAddLoadedModule;
import cloud.terium.networking.packet.node.*;
import cloud.terium.networking.packet.player.*;
import cloud.terium.networking.packet.service.*;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateAdd;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateDelete;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.events.player.CloudPlayerUpdateEvent;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.template.ITemplate;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TeriumNetworkProvider implements IDefaultTeriumNetworking {

    private TeriumClient teriumClient;

    public TeriumNetworkProvider() {
        this.teriumClient = new TeriumClient(NodeStartup.getNode().getNodeConfig().ip(), NodeStartup.getNode().getNodeConfig().port());
        Logger.log("Successfully started terium-client on " + NodeStartup.getNode().getNodeConfig().ip() + ":" + NodeStartup.getNode().getNodeConfig().port() + ".", LogType.INFO);

        getChannel().pipeline().addLast(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet) {
                try {
                    // service packets
                    if (packet instanceof PacketPlayOutServiceAdd newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceAddEvent(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.maxPlayers(), newPacket.memory(), newPacket.node(), newPacket.serviceGroup(), newPacket.templates(), newPacket.propertyCache()));
                    if (packet instanceof PacketPlayOutCreateService newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceCreateEvent(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.maxPlayers(), newPacket.memory(), newPacket.parsedNode().orElseGet(null), newPacket.parsedServiceGroup().orElseGet(null), newPacket.parsedTemplates(), newPacket.propertyCache()));
                    if (packet instanceof PacketPlayOutServiceForceShutdown newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceForceStopEvent(newPacket.serviceName()));
                    if (packet instanceof PacketPlayOutServiceLock newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceLockEvent(newPacket.serviceName()));
                    if (packet instanceof PacketPlayOutSuccessfullyServiceStarted newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceLoggedInEvent(newPacket.serviceName(), newPacket.node()));
                    if (packet instanceof PacketPlayOutServiceRemove newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceRemoveEvent(newPacket.serviceName()));
                    if (packet instanceof PacketPlayOutServiceRestart newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceRestartEvent(newPacket.serviceName()));
                    if (packet instanceof PacketPlayOutServiceStart newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceStartEvent(newPacket.serviceName(), newPacket.node()));
                    if (packet instanceof PacketPlayOutServiceShutdown newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceStopEvent(newPacket.serviceName(), newPacket.node()));
                    if (packet instanceof PacketPlayOutServiceUnlock newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceUnlockEvent(newPacket.serviceName()));
                    if (packet instanceof PacketPlayOutUpdateService newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ServiceUpdateEvent(newPacket.serviceName(), newPacket.players(), newPacket.memory(), newPacket.serviceState(), newPacket.locked(), newPacket.propertyCache()));
                    // player packets
                    if (packet instanceof PacketPlayOutCloudPlayerConnectedService newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CloudPlayerConnectedToServiceEvent(newPacket.cloudPlayer(), newPacket.cloudService()));
                    if (packet instanceof PacketPlayOutCloudPlayerConnect newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CloudPlayerConnectEvent(newPacket.cloudPlayer(), newPacket.cloudService()));
                    if (packet instanceof PacketPlayOutCloudPlayerJoin newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CloudPlayerJoinEvent(newPacket.cloudPlayer()));
                    if (packet instanceof PacketPlayOutCloudPlayerQuit newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CloudPlayerQuitEvent(newPacket.cloudPlayer()));
                    if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CloudPlayerUpdateEvent(newPacket.parsedCloudPlayer().orElseGet(null),
                                newPacket.username(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.parsedCloudService().orElseGet(null)));
                    if (packet instanceof PacketPlayOutCloudPlayerRegister newPacket)
                        NodeStartup.getNode().getCloudPlayerProvider().registerPlayer(newPacket.username(), newPacket.uniquedId(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.cloudService());
                    if (packet instanceof PacketPlayOutCloudPlayerAdd newPacket)
                        NodeStartup.getNode().getNetworking().sendPacket(new PacketPlayOutCloudPlayerAdd(newPacket.username(), newPacket.uniquedId(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.cloudService()));

                    // node packets
                    if (packet instanceof PacketPlayOutNodeStarted newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new NodeLoggedInEvent(newPacket.node()));
                    if (packet instanceof PacketPlayOutNodeShutdown newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new NodeShutdownEvent(newPacket.node()));
                    if (packet instanceof PacketPlayOutNodeShutdowned newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new NodeShutdownedEvent(newPacket.parsedNode().orElseGet(null)));
                    if (packet instanceof PacketPlayOutNodeUpdate newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new NodeUpdateEvent(newPacket.node(), newPacket.usedMemory(), newPacket.maxMemory()));

                    // console packets
                    if (packet instanceof PacketPlayOutSendConsole newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new SendConsoleEvent(newPacket.message(), newPacket.logType()));
                    if (packet instanceof PacketPlayOutRegisterCommand newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new RegisterCommandEvent(newPacket.command()));

                    // group packets
                    if (packet instanceof PacketPlayOutCreateLobbyGroup newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CreateLobbyGroupEvent(newPacket.name(), newPacket.groupTitle(), newPacket.node(), newPacket.fallbackNodes(), newPacket.templates(), newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                    if (packet instanceof PacketPlayOutCreateProxyGroup newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CreateProxyGroupEvent(newPacket.name(), newPacket.groupTitle(), newPacket.node(), newPacket.fallbackNodes(), newPacket.templates(), newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.port(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                    if (packet instanceof PacketPlayOutCreateServerGroup newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new CreateServerGroupEvent(newPacket.name(), newPacket.groupTitle(), newPacket.node(), newPacket.fallbackNodes(), newPacket.templates(), newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                    if (packet instanceof PacketPlayOutGroupDelete newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new DeleteGroupEvent(newPacket.serviceGroup()));
                    if (packet instanceof PacketPlayOutGroupUpdate newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new GroupUpdateEvent(newPacket.serviceGroup()));
                    if (packet instanceof PacketPlayOutGroupReload newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new ReloadGroupEvent(newPacket.serviceGroup()));
                    if (packet instanceof PacketPlayOutGroupsReload)
                        NodeStartup.getNode().getEventProvider().callEvent(new ReloadGroupsEvent());

                    // template events
                    if (packet instanceof cloud.terium.networking.packet.template.PacketPlayOutTemplateCreate newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new TemplateCreateEvent(newPacket.name()));
                    if (packet instanceof PacketPlayOutTemplateDelete newPacket)
                        NodeStartup.getNode().getEventProvider().callEvent(new TemplateDeleteEvent(newPacket.template()));
                } catch (Exception ignored) {}
            }
        });
    }

    @Override
    public Channel getChannel() {
        return teriumClient.getChannel();
    }

    @Override
    public void addHandler(SimpleChannelInboundHandler<Packet> handler) {
        getChannel().pipeline().addLast(handler);
    }

    @Override
    public void sendPacket(Packet packet) {
        this.teriumClient.getChannel().writeAndFlush(packet);
    }
}
