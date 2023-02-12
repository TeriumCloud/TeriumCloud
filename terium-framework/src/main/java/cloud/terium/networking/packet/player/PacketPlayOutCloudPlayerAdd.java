package cloud.terium.networking.packet.player;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

public record PacketPlayOutCloudPlayerAdd(String username, UUID uniquedId, InetSocketAddress address, String cloudService) {

    public Optional<ICloudService> parsedCloudService() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(cloudService);
    }
}