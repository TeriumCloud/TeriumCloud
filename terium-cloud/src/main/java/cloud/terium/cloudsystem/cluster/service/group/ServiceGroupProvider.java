package cloud.terium.cloudsystem.cluster.service.group;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.event.events.group.GroupUpdateEvent;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ServiceGroupProvider implements ICloudServiceGroupProvider {

    private final List<ICloudServiceGroup> serviceGroups;
    private final HashMap<String, ICloudServiceGroup> serviceGroupCache;

    public ServiceGroupProvider() {
        this.serviceGroups = new ArrayList<>();
        this.serviceGroupCache = new HashMap<>();

        new File("groups//").mkdirs();
        for (File file : new File("groups//").listFiles()) {
            initServiceGroup(file);
        }
    }

    public void registerServiceGroup(ICloudServiceGroup serviceGroup) {
        serviceGroups.add(serviceGroup);
        serviceGroupCache.put(serviceGroup.getGroupName(), serviceGroup);
    }

    @SneakyThrows
    public void initServiceGroup(File groupFile) {
        JsonObject serviceGroup = JsonParser.parseReader(new FileReader(groupFile)).getAsJsonObject();
        switch (ServiceType.valueOf(serviceGroup.get("servicetype").getAsString())) {
            case Proxy -> {
                ICloudServiceGroup iCloudServiceGroup = new DefaultProxyGroup(serviceGroup.get("group_name").getAsString(),
                        serviceGroup.get("group_title").getAsString(),
                        ClusterStartup.getCluster().getNodeProvider().getNodeByName(serviceGroup.get("node").getAsString()).orElseGet(null),
                        new LinkedList<>(ClusterStartup.getCluster().getTemplateProvider().getAllTemplates().stream().filter(template -> serviceGroup.get("templates").getAsJsonArray().toString().contains(template.getName())).toList()),
                        serviceGroup.get("version").getAsString(),
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("static").getAsBoolean(),
                        serviceGroup.get("port").getAsInt(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.serviceGroupCache.put(serviceGroup.get("group_name").getAsString(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
            case Server -> {
                ICloudServiceGroup iCloudServiceGroup = new DefaultServerGroup(serviceGroup.get("group_name").getAsString(),
                        serviceGroup.get("group_title").getAsString(),
                        ClusterStartup.getCluster().getNodeProvider().getNodeByName(serviceGroup.get("node").getAsString()).orElseGet(null),
                        new LinkedList<>(ClusterStartup.getCluster().getTemplateProvider().getAllTemplates().stream().filter(template -> serviceGroup.get("templates").getAsJsonArray().toString().contains(template.getName())).toList()),
                        serviceGroup.get("version").getAsString(),
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("static").getAsBoolean(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.serviceGroupCache.put(serviceGroup.get("group_name").getAsString(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
            case Lobby -> {
                ICloudServiceGroup iCloudServiceGroup = new DefaultLobbyGroup(serviceGroup.get("group_name").getAsString(),
                        serviceGroup.get("group_title").getAsString(),
                        ClusterStartup.getCluster().getNodeProvider().getNodeByName(serviceGroup.get("node").getAsString()).orElseGet(null),
                        new LinkedList<>(ClusterStartup.getCluster().getTemplateProvider().getAllTemplates().stream().filter(template -> serviceGroup.get("templates").getAsJsonArray().toString().contains(template.getName())).toList()),
                        serviceGroup.get("version").getAsString(),
                        serviceGroup.get("maintenance").getAsBoolean(),
                        serviceGroup.get("static").getAsBoolean(),
                        serviceGroup.get("maximum_players").getAsInt(),
                        serviceGroup.get("memory").getAsInt(),
                        serviceGroup.get("minimal_services").getAsInt(),
                        serviceGroup.get("maximal_services").getAsInt());
                this.serviceGroupCache.put(serviceGroup.get("group_name").getAsString(), iCloudServiceGroup);
                this.serviceGroups.add(iCloudServiceGroup);
            }
        }

        Logger.log("Loaded service-group '" + serviceGroup.get("group_name").getAsString() + "' successfully.", LogType.INFO);
    }

    public void updateServiceGroup(ICloudServiceGroup serviceGroup) {
        final File file = new File("groups/" + serviceGroup.getGroupName() + ".json");
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            final JsonObject serviceGroupJson = JsonParser.parseReader(reader).getAsJsonObject();
            final JsonArray templateArray = new JsonArray();
            serviceGroup.getTemplates().forEach(template -> templateArray.add(template.getName()));

            serviceGroupJson.addProperty("node", serviceGroup.getGroupNode().getName());
            serviceGroupJson.addProperty("maintenance", serviceGroup.isMaintenance());
            serviceGroupJson.addProperty("static", serviceGroup.isStatic());
            serviceGroupJson.addProperty("version", serviceGroup.getVersion());
            serviceGroupJson.addProperty("memory", serviceGroup.getMemory());
            serviceGroupJson.addProperty("maximum_players", serviceGroup.getMaxPlayers());
            serviceGroupJson.addProperty("minimal_services", serviceGroup.getMinServices());
            serviceGroupJson.addProperty("maximal_services", serviceGroup.getMaxServices());
            serviceGroupJson.add("templates", templateArray);
            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(serviceGroupJson, writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ClusterStartup.getCluster().getEventProvider().callEvent(new GroupUpdateEvent(serviceGroup.getGroupName(), serviceGroup.getGroupNode().getName(), serviceGroup.getVersion(), serviceGroup.getMaxPlayers(), serviceGroup.isMaintenance(), serviceGroup.isStatic(), serviceGroup.getMemory(), serviceGroup.getMinServices(), serviceGroup.getMaxServices()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<ICloudServiceGroup> getServiceGroupByName(String groupName) {
        return Optional.ofNullable(serviceGroupCache.get(groupName));
    }

    @Override
    public int getOnlineServicesFromServiceGroup(String groupName) {
        return ClusterStartup.getCluster().getServiceProvider().getServicesByGroupName(groupName).size();
    }

    @Override
    public List<ICloudServiceGroup> getServiceGroupsByGroupTitle(String groupTitle) {
        return serviceGroups.stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getGroupTitle().equalsIgnoreCase(groupTitle)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getServiceGroupsByNode(String nodeName) {
        return serviceGroups.stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getGroupNode().getName().equalsIgnoreCase(nodeName)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getLobbyGroups() {
        return serviceGroups.stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceType().equals(ServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getProxyGroups() {
        return serviceGroups.stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceType().equals(ServiceType.Proxy)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getServerGroups() {
        return serviceGroups.stream().filter(iCloudServiceGroup -> iCloudServiceGroup.getServiceType().equals(ServiceType.Server)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getAllServiceGroups() {
        return serviceGroups;
    }
}
