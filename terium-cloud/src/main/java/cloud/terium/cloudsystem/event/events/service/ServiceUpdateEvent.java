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

    private final ICloudService cloudService;
    private final HashMap<String, Object> propertyMap;
    private final ServiceState serviceState;
    private final boolean locked;
    private final long memory;
    private final int onlinePlayers;

    public ServiceUpdateEvent(ICloudService cloudService, HashMap<String, Object> propertyMap, ServiceState serviceState, boolean locked, long memory, int onlinePlayers) {
        this.cloudService = cloudService;
        this.serviceState = serviceState;
        this.propertyMap = propertyMap;
        this.locked = locked;
        this.memory = memory;
        this.onlinePlayers = onlinePlayers;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutUpdateService(cloudService, propertyMap, cloudService.isLocked(), cloudService.getServiceState(), cloudService.getOnlinePlayers(), cloudService.getUsedMemory()));
    }
}