package cloud.terium.teriumapi.pipe;

import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;

public interface IDefaultTeriumNetworking {

    Channel getChannel();

    void addHandler(SimpleChannelInboundHandler<Object> handler);

    void addHandler(String name, SimpleChannelInboundHandler<Object> handler);

    void sendPacket(Packet packet);
}