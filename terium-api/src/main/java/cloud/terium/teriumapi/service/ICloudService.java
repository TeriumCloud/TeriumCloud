package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public interface ICloudService {

    String getServiceName();

    boolean isOnline();

    int getServiceId();

    int getPort();

    int getMaxPlayers();

    int getOnlinePlayers();

    int getUsedMemory();

    int getMaxMemory();

    ICloudServiceGroup getServiceGroup();

    CloudServiceType getServiceType();
}
