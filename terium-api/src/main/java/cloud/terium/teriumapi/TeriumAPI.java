package cloud.terium.teriumapi;

import cloud.terium.teriumapi.player.ICloudPlayerManager;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupManager;
import cloud.terium.teriumapi.service.ICloudServiceManager;

public abstract class TeriumAPI {

    private static TeriumAPI instance;

    public TeriumAPI() {
        instance = this;
    }

    public static
    TeriumAPI getInstance() {
        return instance;
    }

    abstract ICloudService getThisService();

    abstract ICloudServiceManager getServiceManager();

    abstract ICloudServiceGroupManager getServiceGroupManager();

    abstract ICloudPlayerManager getCloudPlayerManager();
}