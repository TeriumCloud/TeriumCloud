package cloud.terium.teriumapi.api;

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
}