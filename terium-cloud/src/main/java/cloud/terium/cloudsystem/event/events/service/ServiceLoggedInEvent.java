package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceLoggedInEvent extends Event {

    private final ICloudService cloudService;
    private final INode node;

    public ServiceLoggedInEvent(ICloudService cloudService, INode node) {
        this.cloudService = cloudService;
        this.node = node;
    }
}