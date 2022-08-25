package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.service.CloudServiceType;

public interface ICloudServiceGroup {

    String getServiceGroupName();

    String getGroupTitle();

    String getServiceGroupNode();

    CloudServiceType getServiceType();

    boolean isMaintenance();

    int getPort();

    int getMaximumPlayers();

    int getMemory();

    int getMinimalServices();

    int getMaximalServices();
}
