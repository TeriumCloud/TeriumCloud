package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.service.PacketPlayOutServiceAdd;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class ServiceAddEvent extends Event {

    private final String serviceName;
    private final int serviceId;
    private final int port;
    private final int maxPlayers;
    private final int memory;
    private final String node;
    private final String serviceGroup;
    private final List<String> templates;
    private final HashMap<String, Object> propertyCache;

    public ServiceAddEvent(String serviceName, int serviceId, int port, int maxPlayers, int memory, String node, String serviceGroup, List<String> templates, HashMap<String, Object> propertyCache) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.node = node;
        this.serviceGroup = serviceGroup;
        this.templates = templates;
        this.propertyCache = propertyCache;

        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceAdd(serviceName, serviceId, port, maxPlayers, memory,
                node, serviceGroup, templates, propertyCache));
    }
}