package cloud.terium.teriumapi.events.group;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class CloudGroupUpdatedEvent extends Event {

    private final ICloudServiceGroup cloudServiceGroup;

    public CloudGroupUpdatedEvent(ICloudServiceGroup cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
    }
}
