package cloud.terium.teriumapi.service.group.impl;

import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultProxyGroup implements ICloudServiceGroup {

    private String name;
    private String groupTitle;
    private String node;
    private ITemplate template;
    private final CloudServiceType cloudServiceType = CloudServiceType.Proxy;
    private String version;
    private boolean maintenance;
    private int port;
    private int maximumPlayers;
    private int memory;
    private int minimalServices;
    private int maximalServices;

    @SneakyThrows
    public DefaultProxyGroup(String name, String groupTitle, String node, ITemplate template, String version, boolean maintenance, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.node = node;
        this.template = template;
        this.version = version;
        this.maintenance = maintenance;
        this.port = port;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
    }

    public void initFile() {
        final JsonObject json = new JsonObject();
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        json.addProperty("group_name", name);
        json.addProperty("group_title", groupTitle);
        json.addProperty("node", node);
        json.addProperty("template", template.getName());
        json.addProperty("servicetype", CloudServiceType.Proxy.name());
        json.addProperty("port", port);
        json.addProperty("version", version);
        json.addProperty("maintenance", maintenance);
        json.addProperty("maximum_players", maximumPlayers);
        json.addProperty("memory", memory);
        json.addProperty("minimal_services", minimalServices);
        json.addProperty("maximal_services", maximalServices);

        executorService.execute(() -> {
            try (final OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(new File("groups/" + cloudServiceType + "/" + name + ".json").toPath()), StandardCharsets.UTF_8)) {
                gson.toJson(json, writer);
            } catch (IOException ignored) {
            }
        });
    }

    @Override
    public String getServiceGroupName() {
        return name;
    }

    @Override
    public String getGroupTitle() {
        return groupTitle;
    }

    @Override
    public String getServiceGroupNode() {
        return node;
    }

    @Override
    public ITemplate getTemplate() {
        return template;
    }

    @Override
    public CloudServiceType getServiceType() {
        return cloudServiceType;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isMaintenance() {
        return maintenance;
    }

    @Override
    public boolean hasPort() {
        return true;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    @Override
    public int getMemory() {
        return memory;
    }

    @Override
    public int getMinimalServices() {
        return minimalServices;
    }

    @Override
    public int getMaximalServices() {
        return maximalServices;
    }
}