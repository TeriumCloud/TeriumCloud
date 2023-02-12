package cloud.terium.cloudsystem.event.events.player;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerJoin;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CloudPlayerJoinEvent extends Event {

    private final UUID cloudPlayer;

    public CloudPlayerJoinEvent(UUID cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(cloudPlayer));
    }
}
