package cloud.terium.cloudsystem.common.utils;

import cloud.terium.cloudsystem.common.utils.logger.Logger;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CloudUtils {

    private final List<ICloudPlayer> playerList;
    private final String version;
    private boolean running;
    private boolean firstStart;
    private boolean isInScreen;

    public CloudUtils() {
        this.running = true;
        this.isInScreen = false;
        this.firstStart = false;
        this.version = "1.0.0-OXYGEN(DEVELOPMENT)";
        this.playerList = new ArrayList<>();

        File data = new File("data//versions");
        if (!data.exists()) {
            Logger.log("Downloading spigot.yml, velocity.toml and teriumcloud-plugin...");
            try {
                Logger.log("Trying to download 'spigot.yml'...");
                FileUtils.copyURLToFile(new URL("https://terium.cloud/utils/spigot.yml"), new File("data//versions//spigot.yml"));
                Logger.log("Successfully to downloaded 'spigot.yml'.");
                Thread.sleep(1000);
                Logger.log("Trying to download 'velocity.toml'...");
                FileUtils.copyURLToFile(new URL("https://terium.cloud/utils/velocity.toml"), new File("data//versions//velocity.toml"));
                Logger.log("Successfully to downloaded 'velocity.toml'.");
                Thread.sleep(1000);
                Logger.log("Trying to download 'teriumcloud-plugin.jar'...");
                FileUtils.copyURLToFile(new URL("https://terium.cloud/utils/teriumcloud-plugin.jar"), new File("data//versions//teriumcloud-plugin.jar"));
                Logger.log("Successfully to downloaded 'teriumcloud-plugin.jar'.");
            } catch (IOException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        data.mkdirs();
    }

    public long getUsedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public long getMaxMemory() {
        return (Runtime.getRuntime().maxMemory()) / (1024 * 1024);
    }
}