package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public interface ICloudService {

    String getServiceName();

    default boolean isOnline() {
        return getServiceState() == CloudServiceState.ONLINE;
    }

    int getServiceId();

    int getPort();

    default int getMaxPlayers() {
        return getServiceGroup().getMaximumPlayers();
    }

    int getOnlinePlayers();

    int getUsedMemory();

    default int getMaxMemory() {
        return getServiceGroup().getMemory();
    }

    ICloudServiceGroup getServiceGroup();

    CloudServiceState getServiceState();

    default CloudServiceType getServiceType() {
        return getServiceGroup().getServiceType();
    }

    void setOnlinePlayers(int onlinePlayers);

    void setUsedMemory(int usedMemory);

    void setServiceState(CloudServiceState serviceState);

    default void shutdown() {}

    default void forceShutdown() {}
}
