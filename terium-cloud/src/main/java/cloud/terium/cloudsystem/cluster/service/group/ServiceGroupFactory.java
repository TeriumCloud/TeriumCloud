package cloud.terium.cloudsystem.cluster.service.group;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
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
        ClusterStartup.getCluster().getServiceGroupProvider().getAllServiceGroups().remove(iCloudServiceGroup);
        FileUtils.delete(new File("groups//" + iCloudServiceGroup.getGroupName() + ".json"));
    }
}