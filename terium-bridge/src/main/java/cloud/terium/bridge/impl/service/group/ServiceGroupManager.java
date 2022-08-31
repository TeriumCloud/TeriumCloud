package cloud.terium.bridge.impl.service.group;

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

    public ServiceGroupManager() {
        this.serviceGroups = new ArrayList<>();
        this.registedServerGroups = new HashMap<>();
        loadGroups();
    }

    public void loadGroups() {
        this.file = new File("../../groups/Proxy");
        for (File groupFile : file.listFiles()) {
            initServiceGroup(groupFile);
        }

        this.file = new File("../../groups/Lobby");
        for (File groupFile : file.listFiles()) {
            initServiceGroup(groupFile);
        }

        this.file = new File("../../groups/Server");
        for (File groupFile : file.listFiles()) {
            initServiceGroup(groupFile);
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

    private void reloadServiceGroup(File file, ICloudServiceGroup defaultServiceGroup, boolean port) {
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
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.registedServerGroups.put(serviceGroup.get("group_name").getAsString(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
        }
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


        /*
         * TODO: Code this into terium-api and fix that
         */
        /*switch (update_key) {
            case "memory" -> iCloudServiceGroup.setMemory((Integer) update_value);
            case "maximum_players" -> iCloudServiceGroup.setMaximumPlayers((Integer) update_value);
            case "minimal_services" -> iCloudServiceGroup.setMinimalServices((Integer) update_value);
            case "maximal_services" -> iCloudServiceGroup.setMaximalServices((Integer) update_value);
            case "port" -> iCloudServiceGroup.setPort((Integer) update_value);
            case "maintenance" -> iCloudServiceGroup.setMaintenance((Boolean) update_value);
        }*/
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
        return serviceGroups;
    }
}
