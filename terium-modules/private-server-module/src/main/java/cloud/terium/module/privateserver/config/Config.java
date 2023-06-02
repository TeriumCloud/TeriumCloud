package cloud.terium.module.privateserver.config;

import cloud.terium.teriumapi.TeriumAPI;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Config {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public Config() {
        this.file = new File((TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? "" : "../../") + "modules/private-server/config.json");
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
            JsonObject config = new JsonObject();
            config.addProperty("prefix", "§7[<#06bdf8>Private-Server§7] §f");
            config.addProperty("service-group", "PRIVATE-SERVER");

            json.add("config", config);
            JsonObject messages = new JsonObject();
            messages.addProperty("no.permissions", "§cYou don't have enought permissions to execute this.");
            messages.addProperty("admin.start.private-server", "Private-Server with id '<#06bdf8>%service%' §fis §astarting§7...");
            messages.addProperty("admin.stop.private-server", "Private-Server with id '<#06bdf8>%service%' §fwas §cstopped§7...");
            messages.addProperty("start.private-server", "Your private server is §astarting§7...");
            messages.addProperty("stop.private-server", "Your private server was §cstopped§7.");
            messages.addProperty("add.npc", "Private-Server npc was set at location %location%§7.");
            messages.addProperty("remove.npc", "Private-Server npc was removed from location %location%§7.");
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
}