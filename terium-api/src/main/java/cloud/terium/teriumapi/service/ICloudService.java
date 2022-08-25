package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public interface ICloudService {

    String getServiceName();

    boolean isOnline();

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

    default CloudServiceType getServiceType() {
        return getServiceGroup().getServiceType();
    }

    default void shutdown() {}

    default void forceShutdown() {}

    default void online(boolean online) {}
}
