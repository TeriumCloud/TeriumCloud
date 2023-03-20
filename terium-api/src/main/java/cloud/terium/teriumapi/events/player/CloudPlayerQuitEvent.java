package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import lombok.Getter;

@Getter
public class CloudPlayerQuitEvent extends Event {

    private final ICloudPlayer cloudPlayer;

    public CloudPlayerQuitEvent(ICloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }
}