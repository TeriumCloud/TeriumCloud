package cloud.terium.module.permission.permission.group;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.utils.ApplicationType;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupFileManager {

    private final String groupName;
    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public GroupFileManager(String groupName, ApplicationType applicationType) {
        this.groupName = groupName;
        this.file = new File((applicationType == ApplicationType.MODULE ? "" : "../../") + "modules/permission/groups/" + groupName + ".json");
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
                List<String> permissions = new ArrayList<>();
                List<String> includedGroups = new ArrayList<>();
                json.get("permissions").getAsJsonArray().forEach(jsonElement -> permissions.add(jsonElement.getAsString()));
                json.get("includedGroups").getAsJsonArray().forEach(jsonElement -> includedGroups.add(jsonElement.getAsString()));

                TeriumPermissionModule.getInstance().getPermissionGroupManager().registerGroup(new PermissionGroup(json.get("name").getAsString(),
                        json.get("prefix").getAsString(), json.get("suffix").getAsString(), json.get("tablist").getAsString(), json.get("chatColor").getAsString(),
                        json.get("property").getAsInt(), permissions, includedGroups));
                System.out.println("Successfully loaded group '" + groupName + "'.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("No group with name '" + groupName + "' founded.");
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