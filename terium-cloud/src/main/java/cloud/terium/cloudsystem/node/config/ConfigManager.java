package cloud.terium.cloudsystem.node.config;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.console.LogType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigManager {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public ConfigManager() {
        this.file = new File("config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.initFile();
    }

    @SneakyThrows
    private void initFile() {
        if (!file.exists()) {
            this.json = new JsonObject();
            JsonObject informations = new JsonObject();

            informations.addProperty("name", "Node-1");
            informations.addProperty("ip", NodeStartup.getNode().getIpAddress());
            informations.addProperty("port", 4658);
            json.add("informations", informations);
            json.addProperty("promt", "§b%user%§f@terium => ");
            json.addProperty("memory", 5120);
            json.addProperty("debug", false);
            json.addProperty("checkUpdates", true);
            json.addProperty("serviceAddress", NodeStartup.getNode().getIpAddress());
            json.addProperty("splitter", "-");

            JsonObject masterInfos = new JsonObject();
            masterInfos.addProperty("name", "Master-1");
            masterInfos.addProperty("ip", "127.0.0.1");
            masterInfos.addProperty("port", 4657);
            masterInfos.addProperty("key", "TYPE THE MASTERS KEY HERE!");
            json.add("master", masterInfos);

            save();
            TeriumCloud.getTerium().getCloudUtils().setFirstStart(true);
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public NodeConfig toNodeConfig() {
        try {
            return new NodeConfig(json.get("informations").getAsJsonObject().get("name").getAsString(),
                    json.get("informations").getAsJsonObject().get("ip").getAsString(), json.get("informations").getAsJsonObject().get("port").getAsInt(),
                    json.get("memory").getAsInt(), json.get("serviceAddress").getAsString(), json.get("promt").getAsString(), json.get("splitter").getAsString(),
                    json.get("checkUpdates").getAsBoolean(), json.get("debug").getAsBoolean(), json.get("master").getAsJsonObject());
        } catch (Exception exception) {
            Logger.log("*************************************", LogType.ERROR);
            Logger.log(" ", LogType.ERROR);
            Logger.log("Your config.json seems old! Please delete it to regenerate it.", LogType.ERROR);
            Logger.log(" ", LogType.ERROR);
            Logger.log("*************************************", LogType.ERROR);
            Thread.sleep(2000);
            System.exit(0);
        }

        return null;
    }

    public void save() {
        pool.execute(() -> {
            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                gson.toJson(json, writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public JsonObject getJson() {
        return json;
    }
}