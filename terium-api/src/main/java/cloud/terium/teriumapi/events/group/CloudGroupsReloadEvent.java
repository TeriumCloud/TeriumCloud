package cloud.terium.teriumapi.events.group;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import lombok.Getter;

import java.util.List;

@Getter
public class CloudGroupsReloadEvent extends Event {

    private final List<ICloudServiceGroup> serviceGroups;

    public CloudGroupsReloadEvent(List<ICloudServiceGroup> serviceGroups) {
        this.serviceGroups = serviceGroups;
    }
}