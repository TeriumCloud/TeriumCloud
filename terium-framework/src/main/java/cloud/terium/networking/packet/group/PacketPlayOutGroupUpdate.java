package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.Optional;

public record PacketPlayOutGroupUpdate(String serviceGroup, String node,
                                       String version,
                                       int maximumPlayers,
                                       boolean maintenance,
                                       boolean isStatic,
                                       int memory,
                                       int minimalServices,
                                       int maximalServices) implements Packet {

    public Optional<ICloudServiceGroup> parsedServiceGroup() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(serviceGroup);
    }
}