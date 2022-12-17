package cloud.terium.teriumapi.api;

import cloud.terium.teriumapi.cluster.IClusterProvider;
import cloud.terium.teriumapi.console.ICloudConsoleProvider;
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.template.ITemplateProvider;

public abstract class ICloudProvider {

    /**
     * Use this methode to get the current server as ICloudService
     */
    public abstract ICloudService getThisService();

    /**
     * Use this methode to get the ICloudServiceProvider
     * The ICloudServiceProvider is usefull to interact with CloudServices
     */
    public abstract ICloudServiceProvider getServiceProvider();

    /**
     * Use this methode to get the ICloudServiceProvider
     * The ICloudServiceProvider is usefull to interact with CloudServices
     */
    public abstract ICloudServiceGroupProvider getServiceGroupProvider();

    /**
     * Use this methode to get the ICloudPlayerProvider
     * The ICloudPlayerProvider is usefull to interact with cloud players
     */
    public abstract ICloudPlayerProvider getCloudPlayerProvider();

    /**
     * Use this methode to get the ICloudConsoleProvider
     * The ICloudConsoleProvider is usefull to interact with cloud console
     */
    public abstract ICloudConsoleProvider getConsoleProvider();

    /**
     * Use this methode to get the IEventProvider
     * The IEventProvider is usefull to call events or add register a listener.
     */
    public abstract IEventProvider getEventProvider();

    /**
     * Use this methode to get the IClusterProvider
     * The IEventProvider is usefull to interact with clusters.
     */
    public abstract IClusterProvider getClusterProvider();

    /**
     * Use this methode to get the ITemplateProvider
     * The ITemplateProvider is usefull to interact with templates
     */
    public abstract ITemplateProvider getTemplateProvider();

    /**
     * Use this methode to get the IDefaultTeriumNetworking
     */
    public abstract IDefaultTeriumNetworking getTeriumNetworking();
}