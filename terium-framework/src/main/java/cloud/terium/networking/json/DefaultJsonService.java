package cloud.terium.networking.json;

import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultJsonService implements ICloudService {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;
    private final ICloudService iCloudService;

    public DefaultJsonService(ICloudService iCloudService) {
        this(iCloudService, false);
    }

    public DefaultJsonService(ICloudService iCloudService, boolean bridge) {
        if (!bridge) {
            this.file = new File("data/cache/servers/", iCloudService.getServiceName() + ".json");
        } else {
            this.file = new File("../../data/cache/servers/", iCloudService.getServiceName() + ".json");
        }
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.iCloudService = iCloudService;
        this.initFile();
    }

    public DefaultJsonService(String servicename) {
        this.file = new File("../../data/cache/servers/", servicename + ".json");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.iCloudService = null;
        this.initFile();
    }

    private void initFile() {
        if (!file.exists()) {
            this.json = new JsonObject();
            json.addProperty("service_name", iCloudService.getServiceName());
            json.addProperty("serviceid", iCloudService.getServiceId());
            json.addProperty("port", iCloudService.getPort());
            json.addProperty("online", false);
            json.addProperty("online_players", 0);
            json.addProperty("service_group", iCloudService.getServiceGroup().getServiceGroupName());
            json.addProperty("used_memory", 0);

            save();
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                JsonReader jsonReader = new JsonReader(reader);
                jsonReader.setLenient(true);
                this.json = JsonParser.parseReader(jsonReader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public void delete() {
        FileUtils.forceDelete(this.file);
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

    public String getString(String key) {
        return json.get(key).getAsString();
    }

    public boolean getBoolean(String key) {
        return json.get(key).getAsBoolean();
    }

    public int getInt(String key) {
        return json.get(key).getAsInt();
    }

    public void updateUsedMemory(int memory) {
        json.addProperty("used_memory", memory);
        save();
    }

    public void updateOnlinePlayers(int onlinePlayers) {
        json.addProperty("online_players", onlinePlayers);
        save();
    }

    @Override
    public String getServiceName() {
        return iCloudService.getServiceName();
    }

    @Override
    public boolean isOnline() {
        return json.get("online").getAsBoolean();
    }

    @Override
    public int getServiceId() {
        return iCloudService.getServiceId();
    }

    @Override
    public int getPort() {
        return iCloudService.getPort();
    }

    @Override
    public int getOnlinePlayers() {
        return json.get("online_players").getAsInt();
    }

    @Override
    public int getUsedMemory() {
        return json.get("used_memory").getAsInt();
    }

    @Override
    public ICloudServiceGroup getServiceGroup() {
        return iCloudService.getServiceGroup();
    }
}