package cloud.terium.teriumapi.events.group;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import lombok.Getter;

@Getter
public class CloudGroupCreatedEvent extends Event {

    private final ICloudServiceGroup cloudServiceGroup;

    public CloudGroupCreatedEvent(ICloudServiceGroup cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
    }
}
