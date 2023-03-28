package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;

import java.util.HashMap;
import java.util.Optional;

public record PacketPlayOutUpdateService(String serviceName, int players, double memory, ServiceState serviceState,
                                         boolean locked, HashMap<String, Object> propertyCache) implements Packet {

    public Optional<ICloudService> parsedCloudService() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(serviceName);
    }
}