package cloud.terium.teriumapi.service;

import cloud.terium.networking.packet.group.PacketPlayOutCreateLobbyGroup;
import cloud.terium.networking.packet.group.PacketPlayOutCreateProxyGroup;
import cloud.terium.networking.packet.group.PacketPlayOutCreateServerGroup;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.networking.packet.service.PacketPlayOutUpdateService;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import cloud.terium.teriumapi.service.impl.CloudService;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ServiceBuilder {

    private final String serviceName;
    private final int serviceId;
    private INode node;
    private ICloudServiceGroup serviceGroup;
    private List<ITemplate> templates;
    private final HashMap<String, Object> propertyCache;
    private int maximumPlayers;
    private int maxMemory;

    public ServiceBuilder(String serviceName, int serviceId) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
        this.propertyCache = new HashMap<>();
    }

    public ServiceBuilder setServiceGroup(ICloudServiceGroup serviceGroup) {
        this.serviceGroup = serviceGroup;
        return this;
    }

    public ServiceBuilder addTemplate(ITemplate template) {
        if(templates == null)
            templates = new LinkedList<>();

        templates.add(template);
        return this;
    }

    public ServiceBuilder setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
        return this;
    }

    public ServiceBuilder setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
        return this;
    }

    public ServiceBuilder setNode(INode node) {
        this.node = node;
        return this;
    }

    public ServiceBuilder addProperty(String name, Object property) {
        this.propertyCache.put(name, property);
        return this;
    }

    public ServiceBuilder removeProperty(String name) {
        this.propertyCache.remove(name);
        return this;
    }

    public void build() {
        if (serviceName == null)
            throw new NullPointerException("service name cannot be null");
        if (serviceGroup == null)
            throw new NullPointerException("cloud service group cannot be null");
        if (templates == null)
            templates = new LinkedList<>(serviceGroup.getTemplates());

        TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().createService(serviceName, serviceGroup, templates, serviceId, maximumPlayers, maxMemory, propertyCache);
    }
}