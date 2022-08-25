package cloud.terium.cloudsystem.service.group;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.service.ServiceType;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.setup.SetupState;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupManager;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class ServiceGroupManager implements ICloudServiceGroupManager {

    private File file;
    private Gson gson;
    private ExecutorService pool;
    private JsonObject json;
    private final List<ICloudServiceGroup> serviceGroups;
    private final HashMap<String, ICloudServiceGroup> registedServerGroups;
    private final boolean bridge;

    public ServiceGroupManager() {
        this(false);
    }

    public ServiceGroupManager(boolean bridge) {
        if (bridge) {
            this.file = new File("../../groups/Proxy/");

            if (file.mkdirs()) {
                Logger.log("Successfully init proxy-group folder.", LogType.INFO);
            }

            this.file = new File("../../groups/Lobby/");

            if (file.mkdirs()) {
                Logger.log("Successfully init lobby-group folder.", LogType.INFO);
            }

            this.file = new File("../../groups/Server/");

        } else {
            this.file = new File("groups/Proxy/");

            if (file.mkdirs()) {
                Logger.log("Successfully init proxy-group folder.", LogType.INFO);
            }

            this.file = new File("groups/Lobby/");

            if (file.mkdirs()) {
                Logger.log("Successfully init lobby-group folder.", LogType.INFO);
            }

            this.file = new File("groups/Server/");

        }
        if (file.mkdirs()) {
            Logger.log("Successfully init server-group folder.", LogType.INFO);
        }

        this.bridge = bridge;
        this.serviceGroups = new ArrayList<>();
        this.registedServerGroups = new HashMap<>();
        loadGroups(bridge);
    }

    public void loadGroups(boolean bridge) {
        this.file = bridge ? new File("../../groups/Proxy") : new File("groups/Proxy");
        for (File groupFile : file.listFiles()) {
            initServiceGroup(groupFile, true);
        }

        this.file = bridge ? new File("../../groups/Lobby") : new File("groups/Lobby");
        for (File groupFile : file.listFiles()) {
            initServiceGroup(groupFile);
        }

        this.file = bridge ? new File("../../groups/Server") : new File("groups/Server");
        for (File groupFile : file.listFiles()) {
            initServiceGroup(groupFile);
        }
    }

    @SneakyThrows
    public void reloadGroups(boolean bridge) {
        this.file = bridge ? new File("../../groups/Proxy") : new File("groups/Proxy");
        for (File groupFile : file.listFiles()) {
            reloadGroup(groupFile);
        }

        this.file = bridge ? new File("../../groups/Lobby") : new File("groups/Lobby");
        for (File groupFile : file.listFiles()) {
            reloadGroup(groupFile);
        }

        this.file = bridge ? new File("../../groups/Server") : new File("groups/Server");
        for (File groupFile : file.listFiles()) {
            reloadGroup(groupFile);
        }

        if(!bridge) Logger.log("Successfully reloaded all service groups.", LogType.INFO);
    }

    @SneakyThrows
    public void reloadGroup(File file) {
        JsonObject jsonObject = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
        DefaultServiceGroup serviceGroup = getServiceGroupByName(file.getName().replace(".json", ""));
        serviceGroup.setName(jsonObject.get("group_name").getAsString());
        serviceGroup.setGroupTitle(jsonObject.get("group_title").getAsString());
        serviceGroup.setMaintenance(jsonObject.get("maintenance").getAsBoolean());
        if(serviceGroup.port() != -1) serviceGroup.setPort(jsonObject.get("port").getAsInt());
        serviceGroup.setMaximumPlayers(jsonObject.get("maximum_players").getAsInt());
        serviceGroup.setMinimalServices(jsonObject.get("minimal_services").getAsInt());
        serviceGroup.setMaximalServices(jsonObject.get("maximal_services").getAsInt());
    }

    public void createServiceGroup(DefaultServiceGroup defaultServiceGroup) {
        this.file = new File("groups/" + defaultServiceGroup.serviceType() + "/" + defaultServiceGroup.name() + ".json");

        if (defaultServiceGroup.port() == -1) initFile(file, defaultServiceGroup);
        else initFile(file, defaultServiceGroup, true);

        if(Terium.getTerium().getCloudUtils().getSetupState() == SetupState.DONE) {
            serviceGroups.add(defaultServiceGroup);
            registedServerGroups.put(defaultServiceGroup.name(), defaultServiceGroup);
        }
    }

    private void initFile(File file, DefaultServiceGroup defaultServiceGroup) {
        initFile(file, defaultServiceGroup, false);
    }

    private void initFile(File file, DefaultServiceGroup defaultServiceGroup, boolean port) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pool = Executors.newFixedThreadPool(2);

        if (!file.exists()) {
            try (final PrintWriter writer = new PrintWriter(file)) {
                writer.print(gson.toJson(json = new JsonObject()));
                json.addProperty("group_name", defaultServiceGroup.name());
                json.addProperty("group_title", defaultServiceGroup.groupTitle());
                json.addProperty("servicetype", defaultServiceGroup.serviceType().toString());
                if (port) json.addProperty("port", defaultServiceGroup.port());
                json.addProperty("maintenance", port);
                json.addProperty("maximum_players", defaultServiceGroup.maximumPlayers());
                json.addProperty("memory", defaultServiceGroup.memory());
                json.addProperty("minimal_services", defaultServiceGroup.minimalServices());
                json.addProperty("maximal_services", defaultServiceGroup.maximalServices());

                save();
            } catch (FileNotFoundException ignored) {
            }
        } else {
            try {
                json = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
            } catch (FileNotFoundException ignored) {
            }
        }
    }

    private void reloadServiceGroup(File file, DefaultServiceGroup defaultServiceGroup, boolean port) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pool = Executors.newFixedThreadPool(2);

        if (!file.exists()) {
            try (final PrintWriter writer = new PrintWriter(file)) {
                writer.print(gson.toJson(json = new JsonObject()));
                json.addProperty("group_name", defaultServiceGroup.name());
                json.addProperty("group_title", defaultServiceGroup.groupTitle());
                json.addProperty("servicetype", defaultServiceGroup.serviceType().toString());
                if (port) json.addProperty("port", defaultServiceGroup.port());
                json.addProperty("maintenance", port);
                json.addProperty("maximum_players", defaultServiceGroup.maximumPlayers());
                json.addProperty("memory", defaultServiceGroup.memory());
                json.addProperty("minimal_services", defaultServiceGroup.minimalServices());
                json.addProperty("maximal_services", defaultServiceGroup.maximalServices());

                save();
            } catch (FileNotFoundException ignored) {
            }
        } else {
            try {
                json = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
            } catch (FileNotFoundException ignored) {
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

    @SneakyThrows
    public void initServiceGroup(File groupFile) {
        initServiceGroup(groupFile, false);
    }

    @SneakyThrows
    public void initServiceGroup(File groupFile, CloudServiceType cloudServiceType, boolean port) {
        JsonObject serviceGroup = new JsonParser().parse(new FileReader(groupFile)).getAsJsonObject();
        if(cloudServiceType == CloudServiceType.Proxy) {
            this.registedServerGroups.put(serviceGroup.get("group_name").getAsString(), new DefaultProxyGroup(serviceGroup.get("group_name").getAsString(),
                    serviceGroup.get("group_title").getAsString(),
                    serviceGroup.get("node").getAsString(),
                    serviceGroup.get("maintenance").getAsBoolean(),
                    serviceGroup.get("port").getAsInt(),
                    serviceGroup.get("maximum_players").getAsInt(),
                    serviceGroup.get("memory").getAsInt(),
                    serviceGroup.get("minimal_services").getAsInt(),
                    serviceGroup.get("maximal_services").getAsInt()));
        }

        if (!bridge)
            Logger.log("Loaded service-group '" + serviceGroup.get("group_name").getAsString() + "' successfully.", LogType.INFO);
    }

    /*@SneakyThrows
    public void registerServiceGroup(String groupName) {
        JsonObject serviceGroup = new JsonParser().parse(new FileReader(groupName)).getAsJsonObject();
        this.registedServerGroups.put(serviceGroup.get("group_name").getAsString(), new DefaultServiceGroup(serviceGroup.get("group_name").getAsString(),
                serviceGroup.get("group_title").getAsString(),
                ServiceType.valueOf(serviceGroup.get("servicetype").getAsString()),
                serviceGroup.get("maintenance").getAsBoolean(),
                serviceGroup.get("maximum_players").getAsInt(),
                serviceGroup.get("memory").getAsInt(),
                serviceGroup.get("minimal_services").getAsInt(),
                serviceGroup.get("maximal_services").getAsInt()));
        if (!bridge)
            Logger.log("Loaded service-group '" + serviceGroup.get("group_name").getAsString() + "' successfully.", LogType.INFO);
    }*/

    @SneakyThrows
    public void updateServiceGroup(ICloudServiceGroup iCloudServiceGroup, String update_key, Object update_value) {
        File groupFile = new File("groups/" + iCloudServiceGroup.getServiceType() + "/" + iCloudServiceGroup.getServiceGroupName() + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(groupFile.toPath()), StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if(update_value instanceof String value) json.addProperty(update_key, value);
            else if(update_value instanceof Integer value) json.addProperty(update_key, value);
            else if(update_value instanceof Boolean value) json.addProperty(update_key, value);

            pool.execute(() -> {
                try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(groupFile.toPath()), StandardCharsets.UTF_8)) {
                    gson.toJson(json, writer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            pool.shutdownNow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        switch (update_key) {
            case "memory" -> iCloudServiceGroup.setMemory((Integer) update_value);
            case "maximum_players" -> iCloudServiceGroup.setMaximumPlayers((Integer) update_value);
            case "minimal_services" -> iCloudServiceGroup.setMinimalServices((Integer) update_value);
            case "maximal_services" -> iCloudServiceGroup.setMaximalServices((Integer) update_value);
            case "port" -> iCloudServiceGroup.setPort((Integer) update_value);
            case "maintenance" -> iCloudServiceGroup.setMaintenance((Boolean) update_value);
        }
    }

    @Override
    public ICloudServiceGroup getServiceGroupByName(String s) {
        return registedServerGroups.get(s);
    }

    @Override
    public List<ICloudServiceGroup> getServiceGroupsByGroupTitle(String s) {
        return registedServerGroups.values().stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getGroupTitle().equals(s)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getServiceGroupsByWrapper(String s) {
        return registedServerGroups.values().stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceGroupNode().equals(s)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getLobbyGroups() {
        return registedServerGroups.values().stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceType().equals(CloudServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getProxyGroups() {
        return registedServerGroups.values().stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceType().equals(CloudServiceType.Proxy)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getServerGroups() {
        return registedServerGroups.values().stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceType().equals(CloudServiceType.Server)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getAllServiceGroups() {
        return null;
    }
}