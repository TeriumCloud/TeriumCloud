package cloud.terium.teriumapi.api;

import cloud.terium.teriumapi.console.IConsoleProvider;
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.module.IModuleProvider;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;
import cloud.terium.teriumapi.entity.ICloudPlayerProvider;
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
     * Use this methode to get the node where the service is running.
     */
    public abstract INode getThisNode();

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
     * Use this methode to get the IConsoleProvider
     * The IConsoleProvider is usefull to interact with cloud console
     */
    public abstract IConsoleProvider getConsoleProvider();

    /**
     * Use this methode to get the IEventProvider
     * The IEventProvider is usefull to call events or add register a listener.
     */
    public abstract IEventProvider getEventProvider();

    /**
     * Use this methode to get the IModuleProvider
     * The IModuleProvider is usefull to get informations about a module.
     */
    public abstract IModuleProvider getModuleProvider();

    /**
     * Use this methode to get the INodeProvider
     * The INodeProvider is usefull to interact with nodes.
     */
    public abstract INodeProvider getNodeProvider();

    /**
     * Use this methode to get the ITemplateProvider
     * The ITemplateProvider is usefull to interact with templates
     */
    public abstract ITemplateProvider getTemplateProvider();

    /**
     * Use this methode to get the IDefaultTeriumNetworking
     */
    public abstract IDefaultTeriumNetworking getTeriumNetworking();

    /**
     * Use this methode to get the current version of the cloud
     */
    public abstract String getVersion();
}