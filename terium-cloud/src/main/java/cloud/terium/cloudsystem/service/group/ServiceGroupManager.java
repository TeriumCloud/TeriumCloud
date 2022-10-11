package cloud.terium.cloudsystem.service.group;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.setup.SetupState;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupManager;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
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

            if (file.mkdirs()) {
                Logger.log("Successfully init server-group folder.", LogType.INFO);
            }
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
            initServiceGroup(groupFile);
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

    public void createServiceGroup(ICloudServiceGroup defaultServiceGroup) {
        this.file = new File("groups/" + defaultServiceGroup.getServiceType() + "/" + defaultServiceGroup.getServiceGroupName() + ".json");

        if (defaultServiceGroup.getServiceType().equals(CloudServiceType.Proxy))
            new DefaultProxyGroup(defaultServiceGroup.getServiceGroupName(),
                    defaultServiceGroup.getGroupTitle(),
                    defaultServiceGroup.getServiceGroupNode(),
                    defaultServiceGroup.getVersion(),
                    defaultServiceGroup.isMaintenance(),
                    defaultServiceGroup.getPort(),
                    defaultServiceGroup.getMaximumPlayers(),
                    defaultServiceGroup.getMemory(),
                    defaultServiceGroup.getMinimalServices(),
                    defaultServiceGroup.getMaximalServices());
        else if (defaultServiceGroup.getServiceType().equals(CloudServiceType.Lobby))
            new DefaultLobbyGroup(defaultServiceGroup.getServiceGroupName(),
                    defaultServiceGroup.getGroupTitle(),
                    defaultServiceGroup.getServiceGroupNode(),
                    defaultServiceGroup.getVersion(),
                    defaultServiceGroup.isMaintenance(),
                    defaultServiceGroup.getMaximumPlayers(),
                    defaultServiceGroup.getMemory(),
                    defaultServiceGroup.getMinimalServices(),
                    defaultServiceGroup.getMaximalServices());
        else new DefaultServerGroup(defaultServiceGroup.getServiceGroupName(),
                    defaultServiceGroup.getGroupTitle(),
                    defaultServiceGroup.getServiceGroupNode(),
                    defaultServiceGroup.getVersion(),
                    defaultServiceGroup.isMaintenance(),
                    defaultServiceGroup.getMaximumPlayers(),
                    defaultServiceGroup.getMemory(),
                    defaultServiceGroup.getMinimalServices(),
                    defaultServiceGroup.getMaximalServices());

        if (Terium.getTerium().getCloudUtils().getSetupState() == SetupState.DONE) {
            serviceGroups.add(defaultServiceGroup);
            registedServerGroups.put(defaultServiceGroup.getServiceGroupName(), defaultServiceGroup);
        }
    }

    private void initFile(File file, ICloudServiceGroup defaultServiceGroup) {
        initFile(file, defaultServiceGroup, false);
    }

    private void initFile(File file, ICloudServiceGroup defaultServiceGroup, boolean port) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pool = Executors.newFixedThreadPool(2);

        if (!file.exists()) {
            try (final PrintWriter writer = new PrintWriter(file)) {
                writer.print(gson.toJson(json = new JsonObject()));
                json.addProperty("group_name", defaultServiceGroup.getServiceGroupName());
                json.addProperty("group_title", defaultServiceGroup.getGroupTitle());
                json.addProperty("node", defaultServiceGroup.getServiceGroupNode());
                json.addProperty("servicetype", defaultServiceGroup.getServiceType().toString());
                if (port) json.addProperty("port", defaultServiceGroup.getPort());
                json.addProperty("maintenance", defaultServiceGroup.isMaintenance());
                json.addProperty("maximum_players", defaultServiceGroup.getMaximumPlayers());
                json.addProperty("memory", defaultServiceGroup.getMemory());
                json.addProperty("minimal_services", defaultServiceGroup.getMinimalServices());
                json.addProperty("maximal_services", defaultServiceGroup.getMaximalServices());

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
        JsonObject serviceGroup = new JsonParser().parse(new FileReader(groupFile)).getAsJsonObject();
        switch (CloudServiceType.valueOf(serviceGroup.get("servicetype").getAsString())) {
            case Proxy -> {
                ICloudServiceGroup iCloudServiceGroup = new DefaultProxyGroup(serviceGroup.get("group_name").getAsString(),
                        serviceGroup.get("group_title").getAsString(),
                        serviceGroup.get("node").getAsString(),
                        serviceGroup.get("version").getAsString(),
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("port").getAsInt(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.registedServerGroups.put(serviceGroup.get("group_name").getAsString(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
            case Server -> {
                ICloudServiceGroup iCloudServiceGroup = new DefaultServerGroup(serviceGroup.get("group_name").getAsString(),
                        serviceGroup.get("group_title").getAsString(),
                        serviceGroup.get("node").getAsString(),
                        serviceGroup.get("version").getAsString(),
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.registedServerGroups.put(serviceGroup.get("group_name").getAsString(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
            case Lobby -> {
                ICloudServiceGroup iCloudServiceGroup = new DefaultLobbyGroup(serviceGroup.get("group_name").getAsString(),
                        serviceGroup.get("group_title").getAsString(),
                        serviceGroup.get("node").getAsString(),
                        serviceGroup.get("version").getAsString(),
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.registedServerGroups.put(iCloudServiceGroup.getServiceGroupName(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
        }

        if (!bridge)
            Logger.log("Loaded service-group '" + serviceGroup.get("group_name").getAsString() + "' successfully.", LogType.INFO);
    }

    @SneakyThrows
    public void updateServiceGroup(ICloudServiceGroup iCloudServiceGroup, String update_key, Object update_value) {
        File groupFile = new File("groups/" + iCloudServiceGroup.getServiceType() + "/" + iCloudServiceGroup.getServiceGroupName() + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(groupFile.toPath()), StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (update_value instanceof String value) json.addProperty(update_key, value);
            else if (update_value instanceof Integer value) json.addProperty(update_key, value);
            else if (update_value instanceof Boolean value) json.addProperty(update_key, value);

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
    public List<ICloudServiceGroup> getServiceGroupsByNode(String s) {
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
        return serviceGroups;
    }
}
