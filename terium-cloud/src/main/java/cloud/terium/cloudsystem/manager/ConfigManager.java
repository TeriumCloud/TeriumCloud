package cloud.terium.cloudsystem.manager;

import cloud.terium.cloudsystem.Terium;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    private void initFile() {
        if (!file.exists()) {
            this.json = new JsonObject();
            json.addProperty("ip", "127.0.0.1");
            json.addProperty("web_port", 5124);
            json.addProperty("port", "12345 (don't change)");
            json.addProperty("maxMemory", 10000);
            json.addProperty("license", "put your license here.");
            json.addProperty("node", "Node-01");

            save();
            Terium.getTerium().getCloudUtils().startSetup();
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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

    public void setTeriumPort(int port) {
        json.addProperty("port", port);
        save();
        Terium.getTerium().setConfigManager(this);
    }

    public void resetPort() {
        json.addProperty("port", "12345 (don't change)");
        save();
    }

    public String getString(String key) {
        return json.get(key).getAsString();
    }

    public int getInt(String key) {
        return json.get(key).getAsInt();
    }

    public JsonObject getCloudBridgeConfig() {
        return json.get("terium-bridge").getAsJsonObject();
    }
}