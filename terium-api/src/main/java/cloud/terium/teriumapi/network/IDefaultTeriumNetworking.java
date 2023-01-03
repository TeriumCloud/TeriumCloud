package cloud.terium.teriumapi.network;

import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;

public interface IDefaultTeriumNetworking {

    Channel getChannel();

    void addHandler(SimpleChannelInboundHandler<Packet> handler);

    void sendPacket(Packet packet);
}