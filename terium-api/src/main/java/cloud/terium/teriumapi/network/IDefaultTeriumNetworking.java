package cloud.terium.teriumapi.network;

import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.channels.Channel;

public interface IDefaultTeriumNetworking {

    Channel getChannel();

    void addHandler(SimpleChannelInboundHandler<Packet> handler);

    void sendPacket(Packet packet);
}