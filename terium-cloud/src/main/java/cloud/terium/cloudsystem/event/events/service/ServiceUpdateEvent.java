package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutUpdateService;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ServiceUpdateEvent extends Event {

    private final String cloudService;
    private final int players;
    private final double memory;
    private final ServiceState serviceState;
    private final boolean locked;
    private final HashMap<String, Object> propertyCache;

    public ServiceUpdateEvent(String cloudService, int players, double memory, ServiceState serviceState, boolean locked, HashMap<String, Object> propertyCache) {
        this.cloudService = cloudService;
        this.players = players;
        this.memory = memory;
        this.serviceState = serviceState;
        this.locked = locked;
        this.propertyCache = propertyCache;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutUpdateService(cloudService, players, memory, serviceState, locked, propertyCache));
    }
}