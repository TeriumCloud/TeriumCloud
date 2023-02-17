package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class CloudPlayerUpdateEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final String username;
    private final String address;
    private final ICloudService connectedService;

    public CloudPlayerUpdateEvent(ICloudPlayer cloudPlayer, String username, String address, ICloudService connectedService) {
        this.cloudPlayer = cloudPlayer;
        this.username = username;
        this.address = address;
        this.connectedService = connectedService;
    }
}