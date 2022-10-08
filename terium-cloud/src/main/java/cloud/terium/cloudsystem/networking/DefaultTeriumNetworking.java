package cloud.terium.cloudsystem.networking;

import cloud.terium.cloudsystem.manager.ConfigManager;
import cloud.terium.cloudsystem.networking.server.TeriumServer;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.TeriumFramework;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.Packet;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class DefaultTeriumNetworking {

    private final TeriumServer server;
    private final TeriumClient client;
    private final ConfigManager configManager;
    private final boolean bridge;
    private int port;

    public DefaultTeriumNetworking(ConfigManager configManager) {
        this(false, configManager);
    }

    public DefaultTeriumNetworking(boolean bridge, ConfigManager configManager) {
        this.configManager = configManager;
        this.bridge = bridge;

        if (!bridge) {
            Logger.log("Trying to start terium-server...", LogType.INFO);
            this.port = ThreadLocalRandom.current().nextInt(1024, 65535);
            this.configManager.setTeriumPort(port);
            this.server = new TeriumServer(configManager.getString("ip"), port);
            this.client = null;
            Logger.log("Successfully started terium-server.", LogType.INFO);
            return;
        }

        Logger.log("[TeriumBridge/DefaultTeriumNetworking] Trying to start terium-client...");
        this.client = TeriumFramework.createClient(configManager.getString("ip"), configManager.getInt("port"));
        this.server = null;
        Logger.log("[TeriumBridge/DefaultTeriumNetworking] Successfully started terium-client...");
    }

    public void sendPacket(Packet packet) {
        if (this.server != null) {
            this.server.getChannels().forEach(channel -> channel.writeAndFlush(packet));
        } else {
            this.client.getChannel().writeAndFlush(packet);
        }
    }
}