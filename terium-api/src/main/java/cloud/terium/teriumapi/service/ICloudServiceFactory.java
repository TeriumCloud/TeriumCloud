package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

public interface ICloudServiceFactory {

    /**
     * Create a service with defined service group
     * @param iCloudServiceGroup
     */
    void createService(ICloudServiceGroup iCloudServiceGroup);

    /**
     * Create a service with defined service group and cloud template
     * @param iCloudServiceGroup
     * @param iTemplate
     */
    void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate);

    /**
     * Create a service with defined service name, cloud template, service type, service id and port
     * @param serviceName
     * @param iTemplate
     * @param cloudServiceType
     * @param serviceId
     * @param maxPlayers
     * @param memory
     */
    void createService(String serviceName, ITemplate iTemplate, CloudServiceType cloudServiceType, int serviceId, int maxPlayers, int memory);

    /**
     * Create a service with defined service name and service group
     * @param serviceName
     * @param iCloudServiceGroup
     */
    void createService(String serviceName, ICloudServiceGroup iCloudServiceGroup);

    /**
     * Create a service with defined service name, service group and template
     * @param serviceName
     * @param iCloudServiceGroup
     * @param iTemplate
     */
    void createService(String serviceName, ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate);

    /**
     * Start the defined cloud service
     * @param iCloudService
     */
    void startService(ICloudService iCloudService);
}