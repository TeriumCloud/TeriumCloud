package cloud.terium.cloudsystem.pipe;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.network.Packet;

public class TeriumNetworking implements IDefaultTeriumNetworking {

    private final TeriumServer teriumServer;

    public TeriumNetworking() {
        Logger.log("Trying to start terium-server...", LogType.INFO);
        this.teriumServer = new TeriumServer(TeriumCloud.getTerium().getCloudConfig().ip(), TeriumCloud.getTerium().getCloudConfig().port());
        Logger.log("Successfully started terium-server on " + TeriumCloud.getTerium().getCloudConfig().ip() + ":" + TeriumCloud.getTerium().getCloudConfig().port() + ".", LogType.INFO);
    }

    @Override
    public void sendPacket(Packet packet) {
        this.teriumServer.getChannels().forEach(channel -> channel.writeAndFlush(packet));
    }
}
