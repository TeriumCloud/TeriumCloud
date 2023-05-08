package cloud.terium.module.permission.cloud;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.group.GroupFileManager;
import cloud.terium.module.permission.utils.ApplicationType;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.config.ReloadConfigEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CloudListener implements Listener {

    @Subscribe
    public void handleReloadConfig(ReloadConfigEvent event) {
        TeriumPermissionModule.getInstance().getUserFileManager().loadFile();

        if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null) {
            TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(cloudService -> {
                try {
                    FileUtils.copyDirectory(new File("modules/permission"), new File("servers/" + cloudService.getServiceName() + "/modules/permission"));
                } catch (IOException ignored) { }
            });
        }

        Arrays.stream(new File("modules/permission/groups").listFiles()).toList().forEach(file -> new GroupFileManager(file.getName().replace(".json", ""), ApplicationType.MODULE).loadFile());
    }

    @SneakyThrows
    @Subscribe
    public void handleCloudServiceStarting(CloudServiceStartingEvent event) {
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null) {
            FileUtils.copyDirectory(new File("modules/permission"), new File("servers/" + event.getCloudService().getServiceName() + "/modules/permission"));
        }
    }
}