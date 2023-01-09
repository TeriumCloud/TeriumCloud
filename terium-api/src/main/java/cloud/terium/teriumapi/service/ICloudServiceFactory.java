package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public interface ICloudServiceFactory {

    /**
     * Create a service with defined service group
     *
     * @param serviceGroup
     */
    void createService(ICloudServiceGroup serviceGroup);

    /**
     * Create a service with defined service group and cloud template
     *
     * @param serviceGroup
     * @param templates
     */
    void createService(ICloudServiceGroup serviceGroup, List<ITemplate> templates);

    /**
     * Create a service with defined service name, cloud template, service type, service id and port
     *
     * @param serviceName
     * @param iCloudServiceGroup
     * @param iTemplate
     * @param serviceId
     * @param maxPlayers
     * @param memory
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory);

    /**
     * Create a service with defined service name and service group
     *
     * @param serviceName
     * @param iCloudServiceGroup
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup);

    /**
     * Create a service with defined service name, service group and template
     *
     * @param serviceName
     * @param iCloudServiceGroup
     * @param iTemplate
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates);
}