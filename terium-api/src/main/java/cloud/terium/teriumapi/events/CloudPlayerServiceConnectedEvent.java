package cloud.terium.teriumapi.events;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CloudPlayerServiceConnectedEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final ICloudService cloudService;

    public CloudPlayerServiceConnectedEvent(ICloudPlayer cloudPlayer, ICloudService cloudService) {
        this.cloudPlayer = cloudPlayer;
        this.cloudService = cloudService;
    }
}
