package cloud.terium.teriumapi.api;

import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;

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
}