package cloud.terium.cloudsystem.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        TeriumCloud.getTerium().getNodeProvider().getNodeByName(serviceGroup.get("node").getAsString()),
                        TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().filter(node -> serviceGroup.get("fallback_nodes").getAsJsonArray().toString().contains(node.getName())).toList(),
                        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().stream().filter(template -> serviceGroup.get("templates").getAsJsonArray().toString().contains(template.getName())).toList(),
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
                        TeriumCloud.getTerium().getNodeProvider().getNodeByName(serviceGroup.get("node").getAsString()),
                        TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().filter(node -> serviceGroup.get("fallback_nodes").getAsJsonArray().toString().contains(node.getName())).toList(),
                        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().stream().filter(template -> serviceGroup.get("templates").getAsJsonArray().toString().contains(template.getName())).toList(),
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
                        TeriumCloud.getTerium().getNodeProvider().getNodeByName(serviceGroup.get("node").getAsString()),
                        TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().filter(node -> serviceGroup.get("fallback_nodes").getAsJsonArray().toString().contains(node.getName())).toList(),
                        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().stream().filter(template -> serviceGroup.get("templates").getAsJsonArray().toString().contains(template.getName())).toList(),
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

    @Override
    public ICloudServiceGroup getServiceGroupByName(String groupName) {
        return serviceGroupCache.get(groupName);
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
