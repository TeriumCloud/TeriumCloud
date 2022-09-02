package cloud.terium.teriumapi;

import cloud.terium.teriumapi.player.ICloudPlayerManager;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupManager;
import cloud.terium.teriumapi.service.ICloudServiceManager;

public abstract class TeriumAPI {

    static TeriumAPI instance;

    public TeriumAPI(TeriumAPI teriumAPI) {
        instance = teriumAPI;
    }

    /*
     * Use this methode to get the terium-api instance
     */
     public static TeriumAPI getTeriumAPI() {
         return instance;
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