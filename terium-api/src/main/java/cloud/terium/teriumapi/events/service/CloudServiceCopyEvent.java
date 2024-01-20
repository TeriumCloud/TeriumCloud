package cloud.terium.teriumapi.events.service;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class CloudServiceCopyEvent extends Event {

    private final ICloudService cloudService;
    private final Path template;

    public CloudServiceCopyEvent(ICloudService cloudService, Path template) {
        this.cloudService = cloudService;
        this.template = template;
    }
}