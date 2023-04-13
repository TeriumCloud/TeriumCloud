package cloud.terium.module.syncproxy.manager;

import cloud.terium.module.syncproxy.utils.ApplicationType;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        this.initFile();
    }

    private void initFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void save() {
        System.out.println("No group with name '" + groupName + "' founded.");
    }

    private JsonObject getJson() {
        return json;
    }
}