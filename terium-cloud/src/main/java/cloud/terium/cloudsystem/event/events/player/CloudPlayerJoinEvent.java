package cloud.terium.cloudsystem.event.events.player;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.PacketPlayOutCloudPlayerJoin;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.player.ICloudPlayer;
import lombok.Getter;

@Getter
public class CloudPlayerJoinEvent extends Event {

    private final ICloudPlayer cloudPlayer;

    public CloudPlayerJoinEvent(ICloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(cloudPlayer));
    }
}
