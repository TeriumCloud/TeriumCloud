package cloud.terium.teriumapi.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class CloudServiceStartedEvent extends Event {

    private final ICloudService cloudService;

    public CloudServiceStartedEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
    }
}