package cloud.terium.teriumapi.api;

import cloud.terium.teriumapi.cluster.IClusterFactory;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.template.ITemplateFactory;

public abstract class ICloudFactory {

    /*
     * Use this methode to get the ICloudServiceFactory
     * The ICloudServiceFactory is usefull to create or start services.
     */
    public abstract ICloudServiceFactory getServiceFactory();

    /*
     * Use this methode to get the ICloudServiceGroupFactory
     * The ICloudServiceFactory is usefull to create or delete service groups
     */
    public abstract ICloudServiceGroupFactory getServiceGroupFactory();

    /*
     * Use this methode to get the ITemplateFactory
     * The ITemplateFactory is usefull to create or delete templates
     */
    public abstract ITemplateFactory getTemplateFactory();

    /*
     * Use this methode to get the IClusterFactory
     * The IClusterFactory is usefull to create or delete clusters.
     */
    public abstract IClusterFactory getClusterFactory();

    /*
     * Use this methode to get the ICommandFactory
     * The ICommandFactory is usefull to register self-written commands.
     */
    public abstract ICommandFactory getCommandFactory();
}