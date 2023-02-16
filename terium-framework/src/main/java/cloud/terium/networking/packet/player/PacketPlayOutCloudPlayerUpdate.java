package cloud.terium.networking.packet.player;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.Optional;
import java.util.UUID;

public record PacketPlayOutCloudPlayerUpdate(UUID uniquedId, String username, String address, String cloudService) implements Packet {

    public Optional<ICloudPlayer> parsedCloudPlayer() {
        return TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(uniquedId);
    }

    public Optional<ICloudService> parsedCloudService() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(cloudService);
    }
}
