package cloud.terium.cloudsystem.cluster.pipe;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
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
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.template.ITemplate;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter

public class TeriumServer {

    private final ServerBootstrap serverBootstrap;
    private final Channel channel;
    private final ChannelFuture channelFuture;
    private final List<Channel> channels;

    public TeriumServer(String host, int port) {
        EventLoopGroup eventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            this.serverBootstrap = new ServerBootstrap()
                    .group(eventLoopGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline()
                                    .addLast("packet-decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())))
                                    .addLast("packet-encoder", new ObjectEncoder())
                                    .addLast(new SimpleChannelInboundHandler<>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet) {
                                            try {
                                                // service packets
                                                if (packet instanceof PacketPlayOutServiceAdd newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceAddEvent(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.maxPlayers(), newPacket.memory(), newPacket.node(), newPacket.serviceGroup(), newPacket.templates(), newPacket.propertyCache()));
                                                if (packet instanceof PacketPlayOutCreateService newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceCreateEvent(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.maxPlayers(), newPacket.memory(), newPacket.parsedNode().orElseGet(null), newPacket.parsedServiceGroup().orElseGet(null), newPacket.parsedTemplates(), newPacket.propertyCache()));
                                                if (packet instanceof PacketPlayOutServiceForceShutdown newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceForceStopEvent(newPacket.serviceName()));
                                                if (packet instanceof PacketPlayOutServiceLock newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceLockEvent(newPacket.serviceName()));
                                                if (packet instanceof PacketPlayOutSuccessfullyServiceStarted newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceLoggedInEvent(newPacket.serviceName(), newPacket.node()));
                                                if (packet instanceof PacketPlayOutServiceRemove newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceRemoveEvent(newPacket.serviceName()));
                                                if (packet instanceof PacketPlayOutServiceRestart newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceRestartEvent(newPacket.serviceName()));
                                                if (packet instanceof PacketPlayOutServiceStart newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceStartEvent(newPacket.serviceName(), newPacket.node()));
                                                if (packet instanceof PacketPlayOutServiceShutdown newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceStopEvent(newPacket.serviceName(), newPacket.node()));
                                                if (packet instanceof PacketPlayOutServiceUnlock newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceUnlockEvent(newPacket.serviceName()));
                                                if (packet instanceof PacketPlayOutUpdateService newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ServiceUpdateEvent(newPacket.serviceName(), newPacket.players(), newPacket.memory(), newPacket.serviceState(), newPacket.locked(), newPacket.propertyCache()));
                                                if (packet instanceof PacketPlayOutServiceRegister newPacket) {
                                                    // Nodes
                                                    ClusterStartup.getCluster().getNodeProvider().getAllNodes().forEach(node -> channelHandlerContext.channel().writeAndFlush(
                                                            new PacketPlayOutNodeAdd(node.getName(), node.getKey(), node.getAddress(), node.getMaxMemory(), node.isConnected())));

                                                    // Templates
                                                    ClusterStartup.getCluster().getTemplateProvider().getAllTemplates().forEach(template -> channelHandlerContext.channel().writeAndFlush(
                                                            new PacketPlayOutTemplateAdd(template.getName(), template.getPath().toString())));

                                                    // Groups
                                                    ClusterStartup.getCluster().getServiceGroupProvider().getAllServiceGroups().forEach(group -> channelHandlerContext.channel().writeAndFlush(
                                                            new PacketPlayOutGroupAdd(group.getGroupName(), group.getGroupTitle(), group.getGroupNode().getName(), group.getGroupFallbackNode().stream().map(INode::getName).toList(), group.getTemplates().stream().map(ITemplate::getName).toList(), group.getServiceType(), group.getVersion(),
                                                                    group.isMaintenance(), group.isStatic(), group.hasPort(), group.getPort(), group.getMaxPlayers(), group.getMemory(), group.getMinServices(), group.getMaxServices())));

                                                    // Players
                                                    ClusterStartup.getCluster().getCloudPlayerProvider().getOnlinePlayers().forEach(player -> channelHandlerContext.channel().writeAndFlush(
                                                            new PacketPlayOutCloudPlayerAdd(player.getUsername(), player.getUniqueId(), player.getAddress(), player.getSkinValue(), player.getSkinSignature(), player.getConnectedCloudService().orElseGet(null).getServiceName()
                                                            )));

                                                    // Services
                                                    ClusterStartup.getCluster().getServiceProvider().getAllCloudServices().forEach(cloudService -> channelHandlerContext.channel().writeAndFlush(
                                                            new PacketPlayOutServiceAdd(cloudService.getServiceName(), cloudService.getServiceId(), cloudService.getPort(), cloudService.getMaxPlayers(), cloudService.getMaxMemory(), cloudService.getServiceNode().getName(), cloudService.getServiceGroup().getGroupName(),
                                                                    cloudService.getTemplates().stream().map(ITemplate::getName).toList(), cloudService.getPropertyMap())));
                                                    ClusterStartup.getCluster().getServiceProvider().getAllCloudServices().stream().filter(cloudService -> cloudService.getServiceState().equals(ServiceState.ONLINE)).toList().forEach(cloudService ->
                                                            channelHandlerContext.channel().writeAndFlush(new PacketPlayOutSuccessfullyServiceStarted(cloudService.getServiceName(), cloudService.getServiceNode().getName())));

                                                    // Modules
                                                    ClusterStartup.getCluster().getModuleProvider().getAllModules().forEach(module -> channelHandlerContext.channel().writeAndFlush(
                                                            new PacketPlayOutAddLoadedModule(module.getName(), module.getFileName(), module.getAuthor(), module.getVersion(), module.getDescription(), module.getMainClass(), module.isReloadable(), module.getModuleType()
                                                            )));
                                                }
                                                // player packets
                                                if (packet instanceof PacketPlayOutCloudPlayerConnectedService newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CloudPlayerConnectedToServiceEvent(newPacket.cloudPlayer(), newPacket.cloudService()));
                                                if (packet instanceof PacketPlayOutCloudPlayerConnect newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CloudPlayerConnectEvent(newPacket.cloudPlayer(), newPacket.cloudService()));
                                                if (packet instanceof PacketPlayOutCloudPlayerJoin newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CloudPlayerJoinEvent(newPacket.cloudPlayer()));
                                                if (packet instanceof PacketPlayOutCloudPlayerQuit newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CloudPlayerQuitEvent(newPacket.cloudPlayer()));
                                                if (packet instanceof PacketPlayOutCloudPlayerUpdate newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CloudPlayerUpdateEvent(newPacket.parsedCloudPlayer().orElseGet(null),
                                                            newPacket.username(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.parsedCloudService().orElseGet(null)));
                                                if (packet instanceof PacketPlayOutCloudPlayerRegister newPacket)
                                                    ClusterStartup.getCluster().getCloudPlayerProvider().registerPlayer(newPacket.username(), newPacket.uniquedId(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.cloudService());
                                                if (packet instanceof PacketPlayOutCloudPlayerAdd newPacket)
                                                    ClusterStartup.getCluster().getNetworking().sendPacket(new PacketPlayOutCloudPlayerAdd(newPacket.username(), newPacket.uniquedId(), newPacket.address(), newPacket.value(), newPacket.signature(), newPacket.cloudService()));
                                                if (packet instanceof PacketPlayOutCloudPlayerDisconnect) {
                                                    channels.forEach(targetChannel -> targetChannel.writeAndFlush(packet));
                                                }

                                                // node packets
                                                if (packet instanceof PacketPlayOutNodeStarted newPacket) {
                                                    if(ClusterStartup.getCluster().getNodeProvider().getNodeByName(newPacket.node()).isEmpty()) {
                                                        Logger.log("A NOT REGISTERED NODE WANTS TO CONNECT TO YOUR CLUSTER", LogType.INFO);
                                                        return;
                                                    }

                                                    if (!newPacket.masterKey().equals(ClusterStartup.getCluster().getThisNode().getKey())) {
                                                        channelHandlerContext.channel().close().sync();
                                                        Logger.log("The master key isn't corrent.", LogType.ERROR);
                                                        Logger.log("Closing connection from node '" + newPacket.node() + "'", LogType.ERROR);
                                                        return;
                                                    }

                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new NodeLoggedInEvent(newPacket.node(), newPacket.address(), newPacket.maxMemory(), newPacket.masterKey()));
                                                    ClusterStartup.getCluster().getNodeProvider().addClientToNode(ClusterStartup.getCluster().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), channelHandlerContext);
                                                }
                                                if (packet instanceof PacketPlayOutNodeShutdown newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new NodeShutdownEvent(newPacket.node()));
                                                if (packet instanceof PacketPlayOutNodeShutdowned newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new NodeShutdownedEvent(newPacket.node()));
                                                if (packet instanceof PacketPlayOutNodeUpdate newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new NodeUpdateEvent(newPacket.node(), newPacket.usedMemory(), newPacket.maxMemory()));

                                                // console packets
                                                if (packet instanceof PacketPlayOutSendConsole newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new SendConsoleEvent(newPacket.message(), newPacket.logType()));
                                                if (packet instanceof PacketPlayOutRegisterCommand newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new RegisterCommandEvent(newPacket.command()));

                                                // group packets
                                                if (packet instanceof PacketPlayOutCreateLobbyGroup newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CreateLobbyGroupEvent(newPacket.name(), newPacket.groupTitle(), newPacket.node(), newPacket.fallbackNodes(), newPacket.templates(), newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                                                if (packet instanceof PacketPlayOutCreateProxyGroup newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CreateProxyGroupEvent(newPacket.name(), newPacket.groupTitle(), newPacket.node(), newPacket.fallbackNodes(), newPacket.templates(), newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.port(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                                                if (packet instanceof PacketPlayOutCreateServerGroup newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new CreateServerGroupEvent(newPacket.name(), newPacket.groupTitle(), newPacket.node(), newPacket.fallbackNodes(), newPacket.templates(), newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                                                if (packet instanceof PacketPlayOutGroupDelete newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new DeleteGroupEvent(newPacket.serviceGroup()));
                                                if (packet instanceof PacketPlayOutGroupUpdate newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new GroupUpdateEvent(newPacket.serviceGroup()));
                                                if (packet instanceof PacketPlayOutGroupReload newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ReloadGroupEvent(newPacket.serviceGroup()));
                                                if (packet instanceof PacketPlayOutGroupsReload)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new ReloadGroupsEvent());

                                                // template events
                                                if (packet instanceof cloud.terium.networking.packet.template.PacketPlayOutTemplateCreate newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new TemplateCreateEvent(newPacket.name()));
                                                if (packet instanceof PacketPlayOutTemplateDelete newPacket)
                                                    ClusterStartup.getCluster().getEventProvider().callEvent(new TemplateDeleteEvent(newPacket.template()));
                                            } catch (Exception exception) {
                                                channels.forEach(targetChannel -> {
                                                    if (targetChannel != channelHandlerContext.channel()) {
                                                        targetChannel.writeAndFlush(packet);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void channelRegistered(ChannelHandlerContext channelHandlerContext) {
                                            channels.add(channelHandlerContext.channel());
                                        }

                                        @Override
                                        public void channelUnregistered(ChannelHandlerContext channelHandlerContext) {
                                            channels.remove(channelHandlerContext.channel());
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                        }
                                    });
                        }
                    });
            this.channelFuture = this.serverBootstrap.bind(host, port).sync();
            this.channel = this.channelFuture.channel();
            this.channels = new CopyOnWriteArrayList<>();
        } catch (Exception exception) {
            throw new IllegalStateException
                    ("Failed to start terium-server", exception);
        }
    }
}
