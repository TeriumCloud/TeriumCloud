package cloud.terium.teriumapi;

import cloud.terium.teriumapi.player.ICloudPlayerManager;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupManager;
import cloud.terium.teriumapi.service.ICloudServiceManager;
import lombok.Getter;

public abstract class TeriumAPI {

    @Getter
    protected static TeriumAPI teriumAPI;

    protected TeriumAPI() {
        teriumAPI = this;
    }

    /*
     * Use this methode to get the current server as ICloudService
     */
    public abstract ICloudService getThisService();

    /*
     * Use this methode to get the ICloudServiceManager
     */
    public abstract ICloudServiceManager getServiceManager();

    /*
     * Use this methode to get the ICloudServiceGroupManager
     */
    public abstract ICloudServiceGroupManager getServiceGroupManager();

    /*
     * Use this methode to get the ICloudPlayerManager
     */
    public abstract ICloudPlayerManager getCloudPlayerManager();
}