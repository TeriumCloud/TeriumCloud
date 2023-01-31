package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.Optional;
import java.util.UUID;

public record PacketPlayOutCloudPlayerQuit(ICloudPlayer cloudPlayer) implements Packet {

    @Override
    public ICloudPlayer cloudPlayer() {
        return new ICloudPlayer() {
            @Override
            public String getUsername() {
                return cloudPlayer.getUsername();
            }

            @Override
            public UUID getUniqueId() {
                return cloudPlayer.getUniqueId();
            }

            @Override
            public String getAddress() {
                return cloudPlayer.getAddress();
            }

            @Override
            public Optional<ICloudService> getConnectedCloudService() {
                return Optional.empty();
            }
        };
    }
}