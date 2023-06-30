package cloud.terium.networking.packet.player;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.pipe.Packet;

import java.util.Optional;
import java.util.UUID;

public record PacketPlayOutCloudPlayerJoin(UUID cloudPlayer) implements Packet {

    public Optional<ICloudPlayer> parsedCloudPlayer() {
        return TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(cloudPlayer);
    }
}