package cloud.terium.teriumapi;

import cloud.terium.teriumapi.player.ICloudPlayerManager;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupManager;
import cloud.terium.teriumapi.service.ICloudServiceManager;

public abstract class TeriumAPI {

    /*
     * Use this methode to get the current server as ICloudService
     */
    abstract ICloudService getThisService();

    /*
     * Use this methode to get the ICloudServiceManager
     */
    abstract ICloudServiceManager getServiceManager();

    /*
     * Use this methode to get the ICloudServiceGroupManager
     */
    abstract ICloudServiceGroupManager getServiceGroupManager();

    /*
     * Use this methode to get the ICloudPlayerManager
     */
    abstract ICloudPlayerManager getCloudPlayerManager();
}