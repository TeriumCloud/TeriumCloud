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
            json.addProperty("ip", "127.0.0.1");
            json.addProperty("web_port", 5124);
            json.addProperty("port", "12345 (don't change)");
            json.addProperty("maxMemory", 10000);
            json.addProperty("license", "put your license here.");
            JsonObject cloudbridge = new JsonObject();
            cloudbridge.addProperty("full_kick.message", "<gradient:#245dec:#00d4ff><bold>ᴡᴡᴡ.ᴛᴇʀɪᴜᴍ.ᴄʟᴏᴜᴅ</bold></gradient> \n \n <red>This service is full. \n <red>You can't join without permission.");
            cloudbridge.addProperty("maintenance.message", "<gradient:#245dec:#00d4ff><bold>ᴡᴡᴡ.ᴛᴇʀɪᴜᴍ.ᴄʟᴏᴜᴅ</bold></gradient> \n \n <red>This service is under maintenance. \n <red>More information: https://dsc.gg/jxnnik");
            cloudbridge.addProperty("tablist_header", "\n <gradient:#245dec:#00d4ff>Terium</gradient> <dark_gray>⇨ <white>a modern cloudsystem <gray>[<white>1.0.0-SNAPSHOT<gray>] \n <white>You're connected with <#e2adf7>%server% \n");
            cloudbridge.addProperty("tablist_footer", "\n <white>Terium by <#41dbfa>Jxnnik <white>and <#69e2fa>contributors <red>❤ \n <gradient:#245dec:#00d4ff>ᴡᴡᴡ.ᴛᴇʀɪᴜᴍ.ᴄʟᴏᴜᴅ</gradient> \n");
            cloudbridge.addProperty("motd.line1", "<gradient:#245dec:#00d4ff>Terium</gradient> <dark_gray>⇨ <white>a modern cloudsystem <gray>[<white>1.0.0-SNAPSHOT<gray>]");
            cloudbridge.addProperty("motd.line2", "<gray>› <white>Terium by <#41dbfa>Jxnnik <white>and <#69e2fa>contributors <red>❤");
            cloudbridge.addProperty("motd.maintenance.line1", "<gradient:#b7ffff:#37b4b4>Terium</gradient> <dark_gray>⇨ <white>a modern cloudsystem <gray>[<white>1.0.0-SNAPSHOT<gray>]");
            cloudbridge.addProperty("motd.maintenance.line2", "<gray>› <red>This service is under maintenance.");
            json.add("terium-bridge", cloudbridge);

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