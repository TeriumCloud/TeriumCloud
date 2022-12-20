package cloud.terium.cloudsystem.pipe;

import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.console.LogType;
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
                                    .addLast("packet-decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())))
                                    .addLast("packet-encoder", new ObjectEncoder())
                                    .addLast(new SimpleChannelInboundHandler<Packet>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {

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
            this.channels = new ArrayList<>();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to start terium-server", exception);
        }
    }
}
