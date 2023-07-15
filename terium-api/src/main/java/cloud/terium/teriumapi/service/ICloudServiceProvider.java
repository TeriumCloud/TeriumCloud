package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public interface ICloudServiceProvider {

    /**
     * Get a ICloudService by serviceName
     *
     * @param serviceName The service name of the service you want.
     * @return ICloudService The cloud service you wanted by name.
     */
    Optional<ICloudService> getServiceByName(String serviceName);

    /**
     * Get a List of all ICloudServices by serviceGroup
     *
     * @param serviceGroup The service group of the services you want.
     * @return List<ICloudService> This returns a list of all cloud services from the group.
     */
    List<ICloudService> getServicesByGroupName(String serviceGroup);

    /**
     * Get a List of all ICloudServices by group title
     *
     * @param groupTitle The service group of the services you want.
     * @return List<ICloudService> This returns a list of all cloud services from the group title.
     */
    List<ICloudService> getServicesByGroupTitle(String groupTitle);

    /**
     * Get all ICloudService of type lobby they are connected with the cloud.
     *
     * @return List<ICloudService> This returns a list of all lobby cloud services there are connected with the cloud.
     */
    List<ICloudService> getAllLobbyServices();

    /**
     * Get all ICloudService they are connected with the cloud.
     *
     * @return List<ICloudService> This returns a list of all cloud services there are connected with the cloud.
     */
    List<ICloudService> getAllServices();

    /**
     * This methode is for creating your own CloudService.java.
     * With this methode you can get a free service id from a spefic service group.
     *
     * @param cloudServiceGroup
     * @return
     */
    int getFreeServiceId(ICloudServiceGroup cloudServiceGroup);

    /**
     * This methode return the used memory of the current node.
     *
     * @return used memory of current node
     */
    default long gloablUsedMemory() {
        AtomicLong globalUsedMemory = new AtomicLong();
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream().filter(cloudService -> cloudService.getServiceNode().getName().equals(TeriumAPI.getTeriumAPI().getProvider().getThisNode().getName())).forEach(cloudService -> globalUsedMemory.getAndAdd(cloudService.getMaxMemory()));

        return globalUsedMemory.get();
    }

    /**
     * This methode is for creating your own CloudService.java.
     * With this methode you set the service-id used and unavailable.
     *
     * @param serviceGroup
     * @param id
     */
    void putServiceId(ICloudServiceGroup serviceGroup, int id);

    /**
     * This methode is for creating your own CloudService.java.
     * With this methode you set the service-id unused and available.
     *
     * @param serviceGroup
     * @param id
     */
    void removeServiceId(ICloudServiceGroup serviceGroup, int id);

    /**
     * This methode is for creating your own CloudService.java.
     * With this methode you add your custom service to the #getAllServices() methode.
     *
     * @param cloudService
     */
    void addService(ICloudService cloudService);

    /**
     * This methode is for creating your own CloudService.java.
     * With this methode you remove your custom service from the #getAllServices() methode.
     *
     * @param cloudService
     */
    void removeService(ICloudService cloudService);
}
