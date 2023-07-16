package cloud.terium.module.dockerizedservices.config;

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

public class IncludedGroupsLoader {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public IncludedGroupsLoader() {
        this.file = new File("modules/dockerized-services/included-groups.json");
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
            JsonObject groups = new JsonObject();
            JsonObject exampleGroup = new JsonObject();
            exampleGroup.addProperty("name", "exampleGroup");
            exampleGroup.addProperty("java-image", "openjdk:17-alpine");
            groups.add("exampleGroup", exampleGroup);
            json.add("includedGroups", groups);

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
        return json.get("includedGroups").getAsJsonObject();
    }
}