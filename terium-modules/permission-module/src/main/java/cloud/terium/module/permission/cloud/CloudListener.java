package cloud.terium.module.permission.cloud;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.group.PermissionGroupManager;
import cloud.terium.module.permission.permission.user.UserFileManager;
import cloud.terium.module.permission.utils.ApplicationType;
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
        TeriumPermissionModule.getInstance().getUserFileManager().loadFile();
    }
}