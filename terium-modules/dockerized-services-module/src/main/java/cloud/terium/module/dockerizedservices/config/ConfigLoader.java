package cloud.terium.module.dockerizedservices.config;

import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigLoader {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;
    private IncludedGroupsLoader includedGroupsLoader;

    public ConfigLoader() {
        this.includedGroupsLoader = new IncludedGroupsLoader();
        this.file = new File("modules/dockerized-services/config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.initFile();
    }

    private void initFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            json = new JsonObject();
            json.addProperty("docker-host", "unix:///var/run/docker.sock");
            json.addProperty("docker-cert-path", "null");
            json.addProperty("docker-registry-username", "null");
            json.addProperty("docker-registry-email", "null");
            json.addProperty("docker-registry-password", "null");
            json.addProperty("docker-registry-url", "null");

            save();
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

    public IncludedGroupsLoader getIncludedGroupsLoader() {
        return includedGroupsLoader;
    }
}