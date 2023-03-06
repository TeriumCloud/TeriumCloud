package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class CloudPlayerServiceConnectEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final ICloudService newService;

    public CloudPlayerServiceConnectEvent(ICloudPlayer cloudPlayer, ICloudService newService) {
        this.cloudPlayer = cloudPlayer;
        this.newService = newService;
    }
}
