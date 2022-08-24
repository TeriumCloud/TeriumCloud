package cloud.terium.cloudsystem.template;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TemplateManager {

    /*
     * individual download -> if velocity = download velocity stuff / if paperspigot = download paper etc.
     */

    @SneakyThrows
    public TemplateManager() {
        new File("data//cache//servers//").mkdirs();

        if (new File("templates//Global").mkdirs()) {
            new File("templates//Global//Server").mkdirs();
            new File("templates//Global//Proxy").mkdirs();

            Logger.log("Successfully init template folder.", LogType.INFO);
            Logger.log("Starting of download velocity.toml and spigot.yml", LogType.DOWNLOAD);

            // velocity.toml
            downloadFile("https://api.ipfsbrowser.com/ipfs/download.php?hash=QmNoYuMwDrf1cXrroVr1g87fb9GUv61dCzZfZwSCc4YGrp", "velocity.toml", "templates//Global//Proxy");

            // spigot.yml
            downloadFile("https://api.ipfsbrowser.com/ipfs/download.php?hash=QmQDWghUiH6fs2SqabSZWuceE3GPAjD7bhEeBk6aEiXFbH", "spigot.yml", "templates//Global//Server");
            Logger.log("Successfully downloaded velocity.toml and spigot.yml", LogType.DOWNLOAD);
        }

        File versions = new File("data//versions");

        if (!versions.exists()) {
            versions.mkdirs();

            // TeriumBridge
            Logger.log("Starting download of terium-bridge...", LogType.DOWNLOAD);
            downloadFile("https://workupload.com/start/Z77vYd7dahf", "teriumbridge.jar", "data//versions");
            Logger.log("Successfully downloaded terium-bridge.", LogType.DOWNLOAD);

            // Velocity
            Logger.log("Starting download of velocity...", LogType.DOWNLOAD);
            downloadFile("https://api.papermc.io/v2/projects/velocity/versions/3.1.2-SNAPSHOT/builds/177/downloads/velocity-3.1.2-SNAPSHOT-177.jar", "velocity.jar", "data//versions");
            Logger.log("Successfully downloaded velocity.", LogType.DOWNLOAD);

            // Spigot
            Logger.log("Starting download of spigot...", LogType.DOWNLOAD);
            downloadFile("https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/132/downloads/paper-1.19.2-132.jar", "server.jar", "data//versions");
            Logger.log("Successfully downloaded spigot.", LogType.DOWNLOAD);

            Logger.log(" ", LogType.INFO);
            Logger.log("To complete the setup, you need to restart terium. The cloud will shutdown in 3 secound.", LogType.INFO);
            Logger.log(" ", LogType.INFO);
            Thread.sleep(3000);
            Terium.getTerium().getCloudUtils().shutdownCloud();
        }
    }

    @SneakyThrows
    private void downloadFile(String url, String fileName, String path) {
        InputStream cloudbridge = new URL(url).openStream();
        Files.copy(cloudbridge, Paths.get(path + "//" + fileName), StandardCopyOption.REPLACE_EXISTING);
    }
}