package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceRemoveEvent extends Event {

    private final ICloudService cloudService;

    public ServiceRemoveEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
    }
}