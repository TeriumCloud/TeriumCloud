package cloud.terium.teriumapi.events;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.player.ICloudPlayer;
import lombok.Getter;

@Getter
public class CloudPlayerJoinEvent extends Event {

    private final ICloudPlayer cloudPlayer;

    public CloudPlayerJoinEvent(ICloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }
}