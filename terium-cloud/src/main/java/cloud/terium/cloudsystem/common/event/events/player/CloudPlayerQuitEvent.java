package cloud.terium.cloudsystem.common.event.events.player;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerQuit;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CloudPlayerQuitEvent extends Event {

    private final UUID cloudPlayer;

    public CloudPlayerQuitEvent(UUID cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerQuit(cloudPlayer));
    }
}
