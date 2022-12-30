package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.Getter;

import java.util.List;

@Getter
public class ServiceGroupBuilder {

    private final String name;
    private String groupTitle = "Default service group";
    private INode node = TeriumAPI.getTeriumAPI().getProvider().getThisNode();
    private List<INode> fallbackNodes = List.of();
    private List<ITemplate> templates;
    private final ServiceType serviceType;
    private String version = "paper-1.19.3";
    private int port = 0;
    private boolean maintenance = true;
    private boolean isStatic = false;
    private int maximumPlayers = 20;
    private int memory = 512;
    private int minimalServices = 1;
    private int maximalServices = 2;

    public ServiceGroupBuilder(String name, ServiceType serviceType) {
        this.name = name;
        this.serviceType = serviceType;
        this.templates = List.of(TeriumAPI.getTeriumAPI().getFactory().getTemplateFactory().createTemplate(name));
    }

    public ServiceGroupBuilder setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
        return this;
    }

    public ServiceGroupBuilder setNode(INode node) {
        this.node = node;
        return this;
    }

    public ServiceGroupBuilder setFallbackNodes(List<INode> fallbackNodes) {
        this.fallbackNodes = fallbackNodes;
        return this;
    }

    public ServiceGroupBuilder setTemplates(List<ITemplate> templates) {
        this.templates = templates;
        return this;
    }

    public ServiceGroupBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public ServiceGroupBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public ServiceGroupBuilder setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
        return this;
    }

    public ServiceGroupBuilder setStatic(boolean aStatic) {
        this.isStatic = aStatic;
        return this;
    }

    public ServiceGroupBuilder setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
        return this;
    }

    public ServiceGroupBuilder setMemory(int memory) {
        this.memory = memory;
        return this;
    }

    public ServiceGroupBuilder setMinimalServices(int minimalServices) {
        this.minimalServices = minimalServices;
        return this;
    }

    public ServiceGroupBuilder setMaximalServices(int maximalServices) {
        this.maximalServices = maximalServices;
        return this;
    }

    public ICloudServiceGroup build() {
        return null;
    }
}