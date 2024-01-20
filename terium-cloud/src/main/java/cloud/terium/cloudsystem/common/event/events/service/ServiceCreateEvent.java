package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class ServiceCreateEvent extends Event {

    private final String name;
    private final INode node;
    private final ICloudServiceGroup serviceGroup;
    private final List<ITemplate> templates;
    private final HashMap<String, Object> propertyCache;
    private final int maxPlayers;
    private final int memory;
    private final int serviceId;
    private final int port;
    private final String type;

    public ServiceCreateEvent(String serviceName, int serviceId, int port, int maxPlayers, int memory, INode node, ICloudServiceGroup serviceGroup, List<ITemplate> templates, HashMap<String, Object> propertyCache, String type) {
        this.name = serviceName;
        this.node = node;
        this.serviceGroup = serviceGroup;
        this.templates = templates;
        this.propertyCache = propertyCache;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.serviceId = serviceId;
        this.port = port;
        this.type = type;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, serviceId, port, maxPlayers, memory, node.getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), propertyCache, type));
    }
}