package cloud.terium.cloudsystem.common.event.events.player;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerJoin;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CloudPlayerJoinEvent extends Event {

    private final UUID cloudPlayer;

    public CloudPlayerJoinEvent(UUID cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(cloudPlayer));
    }
}
