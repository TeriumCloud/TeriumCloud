package cloud.terium.module.permission.permission.user;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.utils.ApplicationType;
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
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserFileManager {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public UserFileManager(ApplicationType applicationType) {
        this.file = new File((applicationType == ApplicationType.MODULE ? "" : "../../") + "modules/permission/data/players.json");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
    }

    public void loadFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
                this.json.keySet().forEach(key -> {
                    JsonObject playerObject = this.json.get(key).getAsJsonObject();
                    PermissionUser permissionUser = new PermissionUser(UUID.fromString(playerObject.get("uuid").getAsString()), playerObject.get("username").getAsString(), TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(playerObject.get("group").getAsString()).orElseGet(null));
                    TeriumPermissionModule.getInstance().getPermissionUserManager().registerUser(permissionUser);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            this.json = new JsonObject();
            save();
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