package cloud.terium.teriumapi.service;

import java.util.List;

public interface ICloudServiceManager {

    ICloudService getCloudServiceByName(String serviceName);

    List<ICloudService> getCloudServicesByGroupName(String serviceGroup);

    List<ICloudService> getCloudServicesByGroupTitle(String groupTitle);

    List<ICloudService> getAllCloudServices();
}
