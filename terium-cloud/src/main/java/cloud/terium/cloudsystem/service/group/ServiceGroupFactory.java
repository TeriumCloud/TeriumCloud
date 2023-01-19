package cloud.terium.cloudsystem.service.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class ServiceGroupFactory implements ICloudServiceGroupFactory {

    @SneakyThrows
    @Override
    public void deleteServiceGroup(ICloudServiceGroup iCloudServiceGroup) {
        // stop all services
        TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().remove(iCloudServiceGroup);
        FileUtils.delete(new File("groups//" + iCloudServiceGroup.getGroupName() + ".json"));
    }
}