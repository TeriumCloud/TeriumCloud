package cloud.terium.cloudsystem.networking.server;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.TeriumFramework;
import cloud.terium.networking.packet.codec.PacketDecoder;
import cloud.terium.networking.packet.codec.PacketEncoder;
import cloud.terium.networking.packet.Packet;
import cloud.terium.networking.packets.*;
import cloud.terium.teriumapi.service.CloudServiceState;
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
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new PacketDecoder(TeriumFramework.getPacketLibary()))
                                    .addLast(new PacketEncoder(TeriumFramework.getPacketLibary()))
                                    .addLast(new SimpleChannelInboundHandler<Packet>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
                                            if (packet instanceof PacketPlayOutServiceOnline packetPlayOutServiceOnline) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceOnline.minecraftService()).setServiceState(CloudServiceState.ONLINE);
                                                Logger.log("The service '" + packetPlayOutServiceOnline.minecraftService() + "' successfully started on port " + Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceOnline.minecraftService()).getPort() + ".", LogType.INFO);
                                            }

                                            if (packet instanceof PacketPlayOutServiceForceShutdown packetForce) {
                                                Logger.log("Trying to stop service '" + packetForce.serviceName() + "'...", LogType.INFO);
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetForce.serviceName()).forceShutdown();
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutServiceStart packetPlayOutServiceStart) {
                                                new MinecraftService(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(packetPlayOutServiceStart.defaultServiceGroup())).start();
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutServiceShutdown packetPlayOutServiceShutdown) {
                                                Terium.getTerium().getServiceManager().getCloudServiceByName(packetPlayOutServiceShutdown.minecraftService()).shutdown();
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutCloudPlayerJoin packetJoin) {
                                                Logger.log("The player [" + packetJoin.name() + "/" + packetJoin.uniqueId() + "] trying to connect with the network.", LogType.INFO);
                                                return;
                                            }

                                            if (packet instanceof PacketPlayOutCloudPlayerQuit packetQuit) {
                                                Logger.log("The player [" + packetQuit.name() + "/" + packetQuit.uniqueId() + "] disconnected from the the network.", LogType.INFO);
                                            }

                                            for (Channel channel : channels) {
                                                if (channel != channelHandlerContext.channel()) {
                                                    channel.writeAndFlush(packet);
                                                }
                                            }
                                        }

                                        @Override
                                        public void channelRegistered(ChannelHandlerContext channelHandlerContext) {
                                            channels.add(channel);
                                        }

                                        @Override
                                        public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
                                            channels.remove(channel);
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
