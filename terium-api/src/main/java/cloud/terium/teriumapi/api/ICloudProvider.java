package cloud.terium.teriumapi.api;

import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;

public abstract class ICloudProvider {

    /*
     * Use this methode to get the current server as ICloudService
     */
    public abstract ICloudService getThisService();

    /*
     * Use this methode to get the ICloudServiceProvider
     * The ICloudServiceProvider is usefull to interact with CloudServices
     */
    public abstract ICloudServiceProvider getServiceProvider();

    /*
     * Use this methode to get the ICloudServiceProvider
     * The ICloudServiceProvider is usefull to interact with CloudServices
     */
    public abstract ICloudServiceGroupProvider getServiceGroupProvider();

    /*
     * Use this methode to get the ICloudPlayerManager
     */
    public abstract ICloudPlayerProvider getCloudPlayerProvider();

    /*
     * Use this methode to get the IDefaultTeriumNetworking
     */
    public abstract IDefaultTeriumNetworking getTeriumNetworking();
}