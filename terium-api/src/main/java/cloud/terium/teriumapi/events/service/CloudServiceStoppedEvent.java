package cloud.terium.teriumapi.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class CloudServiceStoppedEvent extends Event {

    private final ICloudService cloudService;

    public CloudServiceStoppedEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
    }
}