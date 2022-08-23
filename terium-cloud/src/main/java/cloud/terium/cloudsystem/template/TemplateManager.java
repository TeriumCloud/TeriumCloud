package cloud.terium.cloudsystem.template;

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

    @SneakyThrows
    public TemplateManager() {
        new File("data//cache//servers//").mkdirs();

        if (new File("templates//Global").mkdirs()) {
            new File("templates//Global//Server").mkdirs();
            new File("templates//Global//Proxy").mkdirs();

            Logger.log("Successfully init template folder.", LogType.INFO);
            Logger.log("Starting of download velocity.toml and spigot.yml", LogType.INFO);

            // velocity.toml
            InputStream velocity = new URL("https://api.ipfsbrowser.com/ipfs/download.php?hash=QmNoYuMwDrf1cXrroVr1g87fb9GUv61dCzZfZwSCc4YGrp").openStream();
            Files.copy(velocity, Paths.get("templates//Global//Proxy//velocity.toml"), StandardCopyOption.REPLACE_EXISTING);

            // spigot.yml
            InputStream spigot = new URL("https://api.ipfsbrowser.com/ipfs/download.php?hash=QmQDWghUiH6fs2SqabSZWuceE3GPAjD7bhEeBk6aEiXFbH").openStream();
            Files.copy(spigot, Paths.get("templates//Global//Server//spigot.yml"), StandardCopyOption.REPLACE_EXISTING);
        }

        File versions = new File("data//versions");

        if (!versions.exists()) {
            versions.mkdirs();

            // CloudBridge
            InputStream cloudbridge = new URL("https://workupload.com/start/Z77vYd7dahf").openStream();
            Files.copy(cloudbridge, Paths.get("data//versions//teriumbridge.jar"), StandardCopyOption.REPLACE_EXISTING);

            // Velocity
            InputStream velocity = new URL("https://api.papermc.io/v2/projects/velocity/versions/3.1.2-SNAPSHOT/builds/172/downloads/velocity-3.1.2-SNAPSHOT-177.jar").openStream();
            Files.copy(velocity, Paths.get("data//versions//velocity.jar"), StandardCopyOption.REPLACE_EXISTING);

            // Spigot
            InputStream spigot = new URL("https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/123/downloads/paper-1.19.2-132.jar").openStream();
            Files.copy(spigot, Paths.get("data//versions//server.jar"), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}