package cloud.terium.teriumproxy.manager;

import com.google.gson.*;

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
        this(false);
    }

    public ConfigManager(boolean bridge) {
        if (!bridge) {
            this.file = new File("config.json");
        } else {
            this.file = new File("../../config.json");
        }
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.initFile();
    }

    private void initFile() {
        if (!file.exists()) {
            this.json = new JsonObject();
            json.addProperty("full_kick.message", "<gradient:#245dec:#00d4ff><bold>ᴡᴡᴡ.ᴛᴇʀɪᴜᴍ.ᴄʟᴏᴜᴅ</bold></gradient> \n \n <red>This service is full. \n <red>You can't join without permission.");
            json.addProperty("maintenance.message", "<gradient:#245dec:#00d4ff><bold>ᴡᴡᴡ.ᴛᴇʀɪᴜᴍ.ᴄʟᴏᴜᴅ</bold></gradient> \n \n <red>This service is under maintenance. \n <red>More information: https://terium.cloud/discord");
            json.addProperty("tablist_header", "\n <gradient:#245dec:#00d4ff>Terium</gradient> <dark_gray>⇨ <white>a modern cloudsystem <gray>[<white>1.0.0-SNAPSHOT<gray>] \n <white>You're connected with <#e2adf7>%server% \n");
            json.addProperty("tablist_footer", "\n <white>Terium by <#41dbfa>Jxnnik <white>and <#69e2fa>contributors <red>❤ \n <gradient:#245dec:#00d4ff>ᴡᴡᴡ.ᴛᴇʀɪᴜᴍ.ᴄʟᴏᴜᴅ</gradient> \n");
            json.addProperty("motd.line1", "<gradient:#245dec:#00d4ff>Terium</gradient> <dark_gray>⇨ <white>a modern cloudsystem <gray>[<white>1.0.0-SNAPSHOT<gray>]");
            json.addProperty("motd.line2", "<gray>› <white>Terium by <#41dbfa>Jxnnik <white>and <#69e2fa>contributors <red>❤");
            json.addProperty("motd.maintenance.line1", "<gradient:#245dec:#00d4ff>Terium</gradient> <dark_gray>⇨ <white>a modern cloudsystem <gray>[<white>1.0.0-SNAPSHOT<gray>]");
            json.addProperty("motd.maintenance.line2", "<gray>› <red>This service is under maintenance.");
            JsonArray whitelist = new JsonArray();
            whitelist.add("ByRaudy");
            whitelist.add("MrsRaudy");
            json.add("whitelist", whitelist);

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

    public String getString(String key) {
        return json.get(key).getAsString();
    }

    public int getInt(String key) {
        return json.get(key).getAsInt();
    }
}