package cloud.terium.cloudsystem.config;

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
            JsonObject master = new JsonObject();
            master.addProperty("name", "Node-1");
            master.addProperty("ip", "127.0.0.1");
            master.addProperty("port", 4657);
            json.add("informations", master);
            json.addProperty("promt", "§b%user%§f@terium => ");
            json.addProperty("memory", 5120);
            json.addProperty("debug", false);
            json.addProperty("serviceAddress", "127.0.0.1");
            json.add("nodes", new JsonObject());

            save();
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public CloudConfig toCloudConfig() {
        return new CloudConfig(json.get("informations").getAsJsonObject().get("name").getAsString(),
                json.get("informations").getAsJsonObject().get("ip").getAsString(), json.get("informations").getAsJsonObject().get("port").getAsInt(),
                json.get("memory").getAsInt(), json.get("serviceAddress").getAsString(), json.get("promt").getAsString(), json.get("debug").getAsBoolean(),
                json.get("nodes").getAsJsonObject());
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