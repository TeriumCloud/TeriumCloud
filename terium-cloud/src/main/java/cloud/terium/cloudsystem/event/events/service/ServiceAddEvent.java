package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceAddEvent extends Event {

    private final ICloudService cloudService;

    public ServiceAddEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
    }
}