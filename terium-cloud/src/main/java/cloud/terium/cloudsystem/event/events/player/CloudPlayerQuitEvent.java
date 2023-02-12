package cloud.terium.cloudsystem.event.events.player;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.PacketPlayOutCloudPlayerQuit;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CloudPlayerQuitEvent extends Event {

    private final UUID cloudPlayer;

    public CloudPlayerQuitEvent(UUID cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCloudPlayerQuit(cloudPlayer));
    }
}
