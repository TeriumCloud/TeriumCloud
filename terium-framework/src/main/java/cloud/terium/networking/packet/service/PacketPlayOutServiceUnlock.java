package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.Optional;

public record PacketPlayOutServiceUnlock(String serviceName) implements Packet {

    public Optional<ICloudService> parsedCloudService() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(serviceName);
    }
}