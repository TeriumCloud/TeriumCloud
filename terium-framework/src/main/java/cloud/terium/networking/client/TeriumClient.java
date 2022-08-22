package cloud.terium.networking.client;

import cloud.terium.networking.TeriumFramework;
import cloud.terium.networking.packet.codec.PacketDecoder;
import cloud.terium.networking.packet.codec.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

@Getter
public class TeriumClient {

    private final Bootstrap bootstrap;
    private final Channel channel;
    private final ChannelFuture channelFuture;

    public TeriumClient(String host, int port) throws Exception {
        EventLoopGroup eventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            this.bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new PacketDecoder(TeriumFramework.getPacketLibary()))
                                    .addLast(new PacketEncoder(TeriumFramework.getPacketLibary()));
                        }
                    });
            this.channelFuture = this.bootstrap.connect(host, port).sync();
            this.channel = this.channelFuture.channel();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to start terium-client", exception);
        }
    }
}