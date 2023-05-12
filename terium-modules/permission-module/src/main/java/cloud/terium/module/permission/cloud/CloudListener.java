package cloud.terium.module.permission.cloud;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.config.ReloadConfigEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class CloudListener implements Listener {

    @Subscribe
    public void handleReloadConfig(ReloadConfigEvent event) {
        TeriumPermissionModule.getInstance().reload();
    }

    @SneakyThrows
    @Subscribe
    public void handleCloudServiceStarting(CloudServiceStartingEvent event) {
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null) {
            FileUtils.copyDirectory(new File("modules/permission"), new File("servers/" + event.getCloudService().getServiceName() + "/modules/permission"));
        }
    }
}