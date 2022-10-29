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
     * Create a service with defined service group, cloud template, start port and max players
     * @param iCloudServiceGroup
     * @param iTemplate
     * @param port
     * @param maxPlayers
     */
    void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate, int port, int maxPlayers);

    /**
     * Create a service with defined service name, cloud template, cloud type and serviceId
     * Max players will be 20 as default
     * @param serviceName
     * @param iTemplate
     * @param cloudServiceType
     * @param serviceId
     * @param memory
     */
    void createService(String serviceName, ITemplate iTemplate, CloudServiceType cloudServiceType, int serviceId, int memory);

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
     * Create a service with defined service name, service group, serviceId and maxPlayers
     * @param serviceName
     * @param iCloudServiceGroup
     * @param serviceId
     * @param maxPlayers
     */
    void createService(String serviceName, ICloudServiceGroup iCloudServiceGroup, int serviceId, int maxPlayers);

    /**
     * Create a service with defined service name, service group and template
     * @param serviceName
     * @param iCloudServiceGroup
     * @param iTemplate
     */
    void createService(String serviceName, ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate);

    /**
     * Create a service with defined service name, service group, template, port and maximum players
     * @param serviceName
     * @param iCloudServiceGroup
     * @param iTemplate
     * @param port
     * @param maxplayers
     */
    void createService(String serviceName, ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate, int port, int maxplayers);

    /**
     * Start the defined cloud service
     * @param iCloudService
     */
    void startService(ICloudService iCloudService);
}