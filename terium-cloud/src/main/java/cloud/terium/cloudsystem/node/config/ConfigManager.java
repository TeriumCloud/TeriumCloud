package cloud.terium.cloudsystem.node.config;

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
import java.util.Random;
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
            JsonObject informations = new JsonObject();

            String keyGenerateString = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
            StringBuilder generatedKey = new StringBuilder();
            for (int i = 0; i < 20; i++) {
                char randompassword = keyGenerateString.toCharArray()[new Random().nextInt(keyGenerateString.length())];
                generatedKey.append(randompassword);
            }

            informations.addProperty("name", "Node-1");
            informations.addProperty("ip", "127.0.0.1");
            informations.addProperty("port", 4657);
            json.add("informations", informations);
            json.addProperty("promt", "§b%user%§f@terium => ");
            json.addProperty("memory", 5120);
            json.addProperty("debug", false);
            json.addProperty("serviceAddress", "127.0.0.1");

            JsonObject masterInfos = new JsonObject();
            masterInfos.addProperty("name", "Master-1");
            masterInfos.addProperty("ip", "127.0.0.1");
            masterInfos.addProperty("port", 4657);
            masterInfos.addProperty("key", "TYPE THE MASTERS KEY HERE!");
            json.add("master", masterInfos);

            save();
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public NodeConfig toNodeConfig() {
        return new NodeConfig(json.get("informations").getAsJsonObject().get("name").getAsString(),
                json.get("informations").getAsJsonObject().get("ip").getAsString(), json.get("informations").getAsJsonObject().get("port").getAsInt(),
                json.get("memory").getAsInt(), json.get("serviceAddress").getAsString(), json.get("promt").getAsString(), json.get("debug").getAsBoolean(),
                json.get("master").getAsJsonObject());
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