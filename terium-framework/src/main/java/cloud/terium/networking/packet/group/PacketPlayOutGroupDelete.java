package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.Optional;

public record PacketPlayOutGroupDelete(String serviceGroup) implements Packet {

    public Optional<ICloudServiceGroup> parsedServiceGroup() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(serviceGroup);
    }
}