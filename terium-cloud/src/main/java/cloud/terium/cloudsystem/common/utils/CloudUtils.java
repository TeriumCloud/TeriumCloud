package cloud.terium.cloudsystem.common.utils;

import cloud.terium.cloudsystem.common.utils.logger.Logger;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
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
    private boolean versionGotChecked;

    @SneakyThrows
    public CloudUtils() {
        this.running = true;
        this.isInScreen = false;
        this.firstStart = false;
        this.versionGotChecked = false;
        this.version = "1.7-OXYGEN";
        this.playerList = new ArrayList<>();

        File data = new File("data//versions");
        if (!data.exists()) {
            Logger.log("Downloading spigot.yml, velocity.toml, config.yml(bungeecord) and teriumcloud-plugin...");
            Logger.log("Trying to download 'spigot.yml'...");
            FileUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/TeriumCloud/external-file-save/main/spigot.yml"), new File("data//versions//spigot.yml"));
            Logger.log("Successfully to downloaded 'spigot.yml'.");
            Thread.sleep(1000);
            Logger.log("Trying to download 'velocity.toml'...");
            FileUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/TeriumCloud/external-file-save/main/velocity.toml"), new File("data//versions//velocity.toml"));
            Logger.log("Successfully to downloaded 'velocity.toml'.");
            Thread.sleep(1000);
            Logger.log("Trying to download 'config.yml'...");
            FileUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/TeriumCloud/external-file-save/main/config.yml"), new File("data//versions//config.yml"));
            Logger.log("Successfully to downloaded 'config.yml'.");
            Thread.sleep(1000);
            Logger.log("Trying to download 'teriumcloud-plugin.jar'...");
            FileUtils.copyURLToFile(new URL("https://github.com/TeriumCloud/external-file-save/raw/main/teriumcloud-plugin.jar"), new File("data//versions//teriumcloud-plugin.jar"));
            Logger.log("Successfully to downloaded 'teriumcloud-plugin.jar'.");
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