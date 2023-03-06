package cloud.terium.cloudsystem.utils;

import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.player.ICloudPlayer;
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
    private boolean setup;
    private boolean isInScreen;

    public CloudUtils() {
        this.running = true;
        this.setup = false;
        this.isInScreen = false;
        this.version = "1.0.0-OXYGEN(DEVELOPMENT)";
        this.playerList = new ArrayList<>();

        File data = new File("data//versions");
        Logger.log("Downloading spigot.yml, velocity.toml and teriumcloud-plugin...");
        if (!data.exists()) {
            try {
                Logger.log("Trying to download 'spigot.yml'...");
                FileUtils.copyURLToFile(new URL("version.getUrl()"), new File("data//versions//spigot.yml"));
                Logger.log("Successfully to downloaded 'spigot.yml'.");
            } catch (IOException exception) {
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