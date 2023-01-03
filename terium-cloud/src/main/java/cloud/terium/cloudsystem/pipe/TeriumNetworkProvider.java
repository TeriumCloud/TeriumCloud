package cloud.terium.cloudsystem.pipe;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.network.Packet;
import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;

public class TeriumNetworkProvider implements IDefaultTeriumNetworking {

    private final TeriumServer teriumServer;

    public TeriumNetworkProvider() {
        Logger.log("Trying to start terium-server...", LogType.INFO);
        this.teriumServer = new TeriumServer(TeriumCloud.getTerium().getCloudConfig().ip(), TeriumCloud.getTerium().getCloudConfig().port());
        Logger.log("Successfully started terium-server on " + TeriumCloud.getTerium().getCloudConfig().ip() + ":" + TeriumCloud.getTerium().getCloudConfig().port() + ".", LogType.INFO);
    }

    @Override
    public Channel getChannel() {
        return teriumServer.getChannel();
    }

    @Override
    public void addHandler(SimpleChannelInboundHandler<Packet> handler) {
        getChannel().pipeline().addLast(handler);
    }

    @Override
    public void sendPacket(Packet packet) {
        this.teriumServer.getChannels().stream().filter(channel -> channel != this.teriumServer.getChannel()).forEach(channel -> channel.writeAndFlush(packet));
    }
}
