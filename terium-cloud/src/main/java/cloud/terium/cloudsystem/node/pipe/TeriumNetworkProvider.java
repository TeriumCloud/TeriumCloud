package cloud.terium.cloudsystem.node.pipe;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.pipe.Packet;
import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;

public class TeriumNetworkProvider implements IDefaultTeriumNetworking {

    private TeriumClient teriumClient;

    public TeriumNetworkProvider() {
        //this.teriumClient = new TeriumClient(NodeStartup.getNode().getNodeConfig().ip(), NodeStartup.getNode().getNodeConfig().port());
        //Logger.log("Successfully started terium-client on " + NodeStartup.getNode().getNodeConfig().ip() + ":" + NodeStartup.getNode().getNodeConfig().port() + ".", LogType.INFO);
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
