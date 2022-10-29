package cloud.terium.cloudsystem.template;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.module.Module;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class TemplateManager implements ITemplateProvider, ITemplateFactory {

    /*
     * individual download -> if velocity = download velocity stuff / if paperspigot = download paper etc.
     */

    private final List<ITemplate> templateList;
    private final HashMap<String, ITemplate> templateCache;

    @SneakyThrows
    public TemplateManager() {
        new File("data//cache//servers//").mkdirs();
        templateList = new ArrayList<>();
        templateCache = new HashMap<>();

        if (new File("templates//Global").mkdirs()) {
            new File("templates//Global//server").mkdirs();
            new File("templates//Global//proxy").mkdirs();

            Logger.log("Successfully init template folder.", LogType.INFO);
            Logger.log("Starting of download velocity.toml and spigot.yml", LogType.DOWNLOAD);

            // velocity.toml
            downloadFile("https://api.ipfsbrowser.com/ipfs/download.php?hash=QmNoYuMwDrf1cXrroVr1g87fb9GUv61dCzZfZwSCc4YGrp", "velocity.toml", "templates//Global//proxy");

            // spigot.yml
            downloadFile("https://api.ipfsbrowser.com/ipfs/download.php?hash=QmQDWghUiH6fs2SqabSZWuceE3GPAjD7bhEeBk6aEiXFbH", "spigot.yml", "templates//Global//server");
            Logger.log("Successfully downloaded velocity.toml and spigot.yml", LogType.DOWNLOAD);
        }

        File versions = new File("data//versions");

        if (!versions.exists()) {
            versions.mkdirs();

            // TeriumBridge
            Logger.log("Starting download of terium-bridge...", LogType.DOWNLOAD);
            downloadFile("https://terium.cloud/download/versions/teriumbridge.jar", "teriumbridge.jar", "data//versions");
            Logger.log("Successfully downloaded terium-bridge.", LogType.DOWNLOAD);

            // Velocity
            Logger.log("Starting download of velocity...", LogType.DOWNLOAD);
            downloadFile("https://api.papermc.io/v2/projects/velocity/versions/3.1.2-SNAPSHOT/builds/184/downloads/velocity-3.1.2-SNAPSHOT-184.jar", "velocity.jar", "data//versions");
            Logger.log("Successfully downloaded velocity.", LogType.DOWNLOAD);

            // Spigot
            Logger.log("Starting download of spigot...", LogType.DOWNLOAD);
            downloadFile("https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/227/downloads/paper-1.19.2-227.jar", "server.jar", "data//versions");
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

    @Override
    public void createTemplate(String s) {
        ITemplate template = new Template(s, new File("templates//" + s).toPath());
        templateList.add(template);
        templateCache.put(s, template);
    }

    @Override
    public void createTemplate(String s, INode iNode) {
        // Send node packet
    }

    @Override
    public void deleteTemplate(String s) {
        // Deleting
    }

    @Override
    public ITemplate getTemplateByName(String s) {
        return templateCache.get(s);
    }

    @Override
    public List<ITemplate> getAllTemplates() {
        return templateList;
    }
}