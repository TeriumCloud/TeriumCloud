package cloud.terium.cloudsystem.network.server;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.TeriumFramework;
import cloud.terium.networking.packet.Packet;
import cloud.terium.networking.packet.codec.PacketDecoder;
import cloud.terium.networking.packet.codec.PacketEncoder;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerJoin;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerQuit;
import cloud.terium.networking.packets.PacketPlayOutSendConsole;
import cloud.terium.networking.packets.PacketPlayOutUpdateService;
import cloud.terium.networking.packets.group.PacketPlayOutCreateLobbyGroup;
import cloud.terium.networking.packets.group.PacketPlayOutCreateProxyGroup;
import cloud.terium.networking.packets.group.PacketPlayOutCreateServerGroup;
import cloud.terium.networking.packets.group.PacketPlayOutGroupDelete;
import cloud.terium.networking.packets.service.*;
import cloud.terium.teriumapi.console.LogType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
                                    .addLast(new PacketDecoder(TeriumFramework.getPacketLibary()))
                                    .addLast(new PacketEncoder(TeriumFramework.getPacketLibary()))
                                    .addLast(new SimpleChannelInboundHandler<Packet>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
                                            if (packet instanceof PacketPlayOutServiceChangeState packetPlayOutServiceChangeState) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceChangeState.minecraftService()).setServiceState(packetPlayOutServiceChangeState.serviceState());
                                                Logger.log("The service '" + packetPlayOutServiceChangeState.minecraftService() + "' successfully started on port " + Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceChangeState.minecraftService()).getPort() + ".", LogType.INFO);
                                            }

                                            if (packet instanceof PacketPlayOutServiceMemoryUpdatePacket packetPlayOutServiceMemoryUpdatePacket) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceMemoryUpdatePacket.minecraftService()).setUsedMemory(packetPlayOutServiceMemoryUpdatePacket.memory());
                                            }

                                            if (packet instanceof PacketPlayOutServiceOnlinePlayersUpdatePacket packetPlayOutServiceOnlinePlayersUpdatePacket) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceOnlinePlayersUpdatePacket.minecraftService()).setOnlinePlayers(packetPlayOutServiceOnlinePlayersUpdatePacket.players());
                                            }

                                            if(packet instanceof PacketPlayOutCreateProxyGroup packetPlayOutCreateProxyGroup) {
                                                Terium.getTerium().getServiceGroupManager().createProxyGroup(packetPlayOutCreateProxyGroup.getName(), packetPlayOutCreateProxyGroup.getGroupTitle(), packetPlayOutCreateProxyGroup.getNode(),
                                                        Terium.getTerium().getTemplateManager().getTemplateByName(packetPlayOutCreateProxyGroup.getTemplate()), packetPlayOutCreateProxyGroup.getVersion(), packetPlayOutCreateProxyGroup.getPort(),
                                                        packetPlayOutCreateProxyGroup.getMaximumPlayers(), packetPlayOutCreateProxyGroup.getMemory(), packetPlayOutCreateProxyGroup.getMinimalServices(), packetPlayOutCreateProxyGroup.getMaximalServices());
                                            }

                                            if(packet instanceof PacketPlayOutCreateLobbyGroup packetPlayOutCreateProxyGroup) {
                                                Terium.getTerium().getServiceGroupManager().createLobbyGroup(packetPlayOutCreateProxyGroup.getName(), packetPlayOutCreateProxyGroup.getGroupTitle(), packetPlayOutCreateProxyGroup.getNode(),
                                                        Terium.getTerium().getTemplateManager().getTemplateByName(packetPlayOutCreateProxyGroup.getTemplate()), packetPlayOutCreateProxyGroup.getVersion(),
                                                        packetPlayOutCreateProxyGroup.getMaximumPlayers(), packetPlayOutCreateProxyGroup.getMemory(), packetPlayOutCreateProxyGroup.getMinimalServices(), packetPlayOutCreateProxyGroup.getMaximalServices());
                                            }

                                            if(packet instanceof PacketPlayOutCreateServerGroup packetPlayOutCreateProxyGroup) {
                                                Terium.getTerium().getServiceGroupManager().createServerGroup(packetPlayOutCreateProxyGroup.getName(), packetPlayOutCreateProxyGroup.getGroupTitle(), packetPlayOutCreateProxyGroup.getNode(),
                                                        Terium.getTerium().getTemplateManager().getTemplateByName(packetPlayOutCreateProxyGroup.getTemplate()), packetPlayOutCreateProxyGroup.getVersion(),
                                                        packetPlayOutCreateProxyGroup.getMaximumPlayers(), packetPlayOutCreateProxyGroup.getMemory(), packetPlayOutCreateProxyGroup.getMinimalServices(), packetPlayOutCreateProxyGroup.getMaximalServices());
                                            }

                                            if (packet instanceof PacketPlayOutGroupDelete packetPlayOutGroupDelete) {
                                                Terium.getTerium().getServiceGroupManager().deleteServiceGroup(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(packetPlayOutGroupDelete.serviceGroup()));
                                            }

                                            /*
                                             * TODO: RECODE SERVICE STOPPING
                                             */
                                            /*if (packet instanceof PacketPlayOutServiceForceShutdown packetForce) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetForce.serviceName()).shutdown();
                                                return;
                                            }*/

                                            if (packet instanceof PacketPlayOutSendConsole console) {
                                                Logger.log(console.message(), console.logType());
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutUpdateService updateService) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(updateService.servicename()).setLocked(updateService.locked());
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(updateService.servicename()).setServiceState(updateService.serviceState());
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(updateService.servicename()).setOnlinePlayers(updateService.onlinePlayers());
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(updateService.servicename()).setUsedMemory(updateService.usedMemory());
                                            }

                                            if (packet instanceof PacketPlayOutServiceLock packetPlayOutServiceLock) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceLock.minecraftService()).setLocked(true);
                                            }

                                            if (packet instanceof PacketPlayOutServiceUnlock packetPlayOutServiceUnlock) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceUnlock.minecraftService()).setLocked(false);
                                            }

                                            if (packet instanceof PacketPlayOutServiceStart packetPlayOutServiceStart) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceStart.serviceName()).start();
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutServiceShutdown packetPlayOutServiceShutdown) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceShutdown.minecraftService()).shutdown();
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutServiceRestart packetPlayOutServiceShutdown) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceShutdown.serviceName()).restart();
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutCloudPlayerJoin packetJoin) {
                                                Logger.log("The player [" + packetJoin.name() + "/" + packetJoin.uniqueId() + "] trying to connect with the network.", LogType.INFO);
                                                channelHandlerContext.channel().writeAndFlush(packet);
                                            }

                                            if (packet instanceof PacketPlayOutCloudPlayerQuit packetQuit) {
                                                Logger.log("The player [" + packetQuit.name() + "/" + packetQuit.uniqueId() + "] disconnected from the the network.", LogType.INFO);
                                                channelHandlerContext.channel().writeAndFlush(packet);
                                            }

                                            channels.forEach(targetChannel -> {
                                                if (targetChannel != channelHandlerContext.channel()) {
                                                    targetChannel.writeAndFlush(packet);
                                                }
                                            });
                                        }

                                        @Override
                                        public void channelRegistered(ChannelHandlerContext channelHandlerContext) {
                                            channels.add(channelHandlerContext.channel());
                                        }

                                        @Override
                                        public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
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
            this.channels = new ArrayList<>();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to start cloud server", exception);
        }
    }
}
