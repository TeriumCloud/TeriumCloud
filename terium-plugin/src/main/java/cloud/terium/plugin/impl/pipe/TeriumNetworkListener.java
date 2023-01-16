package cloud.terium.plugin.pipe;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.network.Packet;
import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;

public class TeriumNetworkListener implements IDefaultTeriumNetworking {

    private final TeriumClient teriumClient;

    @SneakyThrows
    public TeriumNetworkListener() {
        teriumClient = new TeriumClient("127.0.0.1", 1234);
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
        teriumClient.getChannel().writeAndFlush(packet);
    }
}
