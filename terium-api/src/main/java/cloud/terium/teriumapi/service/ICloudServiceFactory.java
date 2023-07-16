package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.HashMap;
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
     * @param serviceGroup
     * @param templates
     * @param serviceId
     * @param maxPlayers
     * @param memory
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory);

    /**
     * Create a service with defined service name, cloud template, service type, service id, port and own property cache
     *
     * @param serviceName
     * @param serviceGroup
     * @param templates
     * @param serviceId
     * @param maxPlayers
     * @param memory
     * @param propertyCache
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory, HashMap<String, Object> propertyCache);

    /**
     * Create a service with defined service name and service group
     *
     * @param serviceName
     * @param serviceGroup
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup);

    /**
     * Create a service with defined service name, service group and template
     *
     * @param serviceName
     * @param serviceGroup
     * @param templates
     */
    void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates);

    /**
     * Checks if the defined service group is binded to this service factory.
     *
     * @param cloudServiceGroup
     * @return boolean
     */
    boolean containsServiceGroup(ICloudServiceGroup cloudServiceGroup);

    /**
     * Add a service group who starts the service over this service factory.
     *
     * @param cloudServiceGroup
     */
    void bindServiceGroup(ICloudServiceGroup cloudServiceGroup);

    /**
     * Remove a service group who starts the service over this service factory.
     *
     * @param cloudServiceGroup
     */
    void unbindServiceGroup(ICloudServiceGroup cloudServiceGroup);
}