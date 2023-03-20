package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class CloudPlayerServiceConnectedEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final ICloudService cloudService;

    public CloudPlayerServiceConnectedEvent(ICloudPlayer cloudPlayer, ICloudService cloudService) {
        this.cloudPlayer = cloudPlayer;
        this.cloudService = cloudService;
    }
}
