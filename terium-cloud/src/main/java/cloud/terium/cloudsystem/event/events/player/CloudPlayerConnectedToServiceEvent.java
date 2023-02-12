package cloud.terium.cloudsystem.event.events.player;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.PacketPlayOutCloudPlayerConnectedService;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CloudPlayerConnectedToServiceEvent extends Event {

    private final UUID cloudPlayer;
    private final String cloudService;

    public CloudPlayerConnectedToServiceEvent(UUID cloudPlayer, String cloudService) {
        this.cloudPlayer = cloudPlayer;
        this.cloudService = cloudService;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCloudPlayerConnectedService(cloudPlayer, cloudService));
    }
}
