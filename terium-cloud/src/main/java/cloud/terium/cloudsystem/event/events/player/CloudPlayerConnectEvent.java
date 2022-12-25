package cloud.terium.cloudsystem.event.events.player;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.PacketPlayOutCloudPlayerConnect;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class CloudPlayerConnectEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final ICloudService cloudService;

    public CloudPlayerConnectEvent(ICloudPlayer cloudPlayer, ICloudService cloudService) {
        this.cloudPlayer = cloudPlayer;
        this.cloudService = cloudService;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(cloudPlayer, cloudService));
    }
}
