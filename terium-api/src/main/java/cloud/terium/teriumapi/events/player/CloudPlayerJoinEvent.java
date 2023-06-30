package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class CloudPlayerJoinEvent extends Event {

    private final ICloudPlayer cloudPlayer;

    public CloudPlayerJoinEvent(ICloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }
}