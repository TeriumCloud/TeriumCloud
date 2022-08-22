package cloud.terium.cloudsystem.networking.json;

import cloud.terium.cloudsystem.service.IService;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.service.ServiceType;
import cloud.terium.cloudsystem.service.group.DefaultServiceGroup;
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

public class DefaultJsonService implements IService {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;
    private final MinecraftService minecraftService;

    public DefaultJsonService(MinecraftService minecraftService) {
        this(minecraftService, false);
    }

    public DefaultJsonService(MinecraftService minecraftService, boolean bridge) {
        if (!bridge) {
            this.file = new File("data/cache/servers/", minecraftService.serviceName() + ".json");
        } else {
            this.file = new File("../../data/cache/servers/", minecraftService.serviceName() + ".json");
        }
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.minecraftService = minecraftService;
        this.initFile();
    }

    public DefaultJsonService(String servicename) {
        this.file = new File("../../data/cache/servers/", servicename + ".json");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.minecraftService = null;
        this.initFile();
    }

    private void initFile() {
        if (!file.exists()) {
            this.json = new JsonObject();
            json.addProperty("service_name", minecraftService.serviceName());
            json.addProperty("serviceid", minecraftService.getServiceId());
            json.addProperty("port", minecraftService.getPort());
            json.addProperty("online", false);
            json.addProperty("online_players", 0);
            json.addProperty("service_group", minecraftService.getDefaultServiceGroup().name());
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
    public String serviceName() {
        return minecraftService.serviceName();
    }

    @Override
    public boolean online() {
        return json.get("online").getAsBoolean();
    }

    @Override
    public int serviceId() {
        return minecraftService.serviceId();
    }

    @Override
    public int port() {
        return minecraftService.port();
    }

    @Override
    public int maxPlayers() {
        return minecraftService.maxPlayers();
    }

    @Override
    public int onlinePlayers() {
        return json.get("online_players").getAsInt();
    }

    @Override
    public int usedMemory() {
        return json.get("used_memory").getAsInt();
    }

    @Override
    public int maxMemory() {
        return minecraftService.defaultServiceGroup().memory();
    }

    @Override
    public DefaultServiceGroup defaultServiceGroup() {
        return minecraftService.defaultServiceGroup();
    }

    @Override
    public ServiceType serviceType() {
        return defaultServiceGroup().serviceType();
    }
}