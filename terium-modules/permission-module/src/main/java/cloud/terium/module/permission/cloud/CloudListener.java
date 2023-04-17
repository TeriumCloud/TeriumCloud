package cloud.terium.module.permission.cloud;

import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class CloudListener implements Listener {

    @SneakyThrows
    @Subscribe
    public void handleService(CloudServiceStartingEvent event) {
        FileUtils.copyDirectory(new File("modules/permission/groups"), new File("servers/" + event.getCloudService().getServiceName() + "/modules/permission/groups"));
    }
}