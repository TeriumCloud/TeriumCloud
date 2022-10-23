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
     * Create a service with defined service name and cloud template
     * @param serviceName
     * @param iTemplate
     */
    void createService(String serviceName, ITemplate iTemplate);

    /**
     * Create a service with defined service name and service group
     * @param serviceName
     * @param iCloudServiceGroup
     */
    void createService(String serviceName, ICloudServiceGroup iCloudServiceGroup);

    /**
     * Start the defined cloud service
     * @param iCloudService
     */
    void startService(ICloudService iCloudService);
}