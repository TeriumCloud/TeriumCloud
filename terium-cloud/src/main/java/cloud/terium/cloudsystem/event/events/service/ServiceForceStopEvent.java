package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceForceStopEvent extends Event {

    private final ICloudService cloudService;

    public ServiceForceStopEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
    }
}