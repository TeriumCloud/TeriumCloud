package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.player.ICloudPlayer;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CloudPlayerServiceConnectEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final Optional<ICloudService> oldService;
    private final ICloudService newService;

    public CloudPlayerServiceConnectEvent(ICloudPlayer cloudPlayer, Optional<ICloudService> oldService, ICloudService newService) {
        this.cloudPlayer = cloudPlayer;
        this.oldService = oldService;
        this.newService = newService;
    }
}
