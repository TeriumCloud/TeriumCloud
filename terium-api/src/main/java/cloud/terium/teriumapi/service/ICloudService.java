package cloud.terium.teriumapi.service;

import cloud.terium.networking.packet.service.PacketPlayOutCopyServiceToTemplate;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface ICloudService extends Serializable {

    /**
     * Get the service name of a serivce
     *
     * @return String This returns the service name as String.
     */
    String getServiceName();

    /**
     * Get the service id of a service
     *
     * @return int This returns the sevice id of the service.
     */
    int getServiceId();

    /**
     * Get the port of the current service
     *
     * @return int This returns the port of the service.
     */
    int getPort();

    /**
     * Get the ICloudServiceGroup of the serivce
     *
     * @return ICloudServiceGroup This returns the ICloudServiceGroup of the service.
     */
    ICloudServiceGroup getServiceGroup();

    /**
     * Get the INode of the serivce
     *
     * @return INode This returns the INode of the service.
     */
    INode getServiceNode();

    /**
     * Get the templates of the current service
     *
     * @return int This returns all templates of the service as list.
     */
    List<ITemplate> getTemplates();

    /**
     * Get the maximal players who can join on the service
     *
     * @return int This returns the maximal player who can join as int.
     */
    default int getMaxPlayers() {
        return getServiceGroup().getMaxPlayers();
    }

    /**
     * Get the online players on the service
     *
     * @return int This returns the online players as int.
     */
    int getOnlinePlayers();

    /**
     * Set the online players of the service.
     *
     * @param onlinePlayers
     */
    void setOnlinePlayers(int onlinePlayers);

    /**
     * Get the used memory from the service
     *
     * @return int This returns the used memory from the service as long.
     */
    long getUsedMemory();

    /**
     * Set the used memory of the service.
     *
     * @param usedMemory
     */
    void setUsedMemory(long usedMemory);

    /**
     * Get the max memory of the service
     *
     * @return int This returns the maximal memory of the service as int.
     */
    default int getMaxMemory() {
        return getServiceGroup().getMemory();
    }

    /**
     * Get the ServiceState of the service
     *
     * @return CloudServiceGroup This returns the current service state.
     */
    ServiceState getServiceState();

    /**
     * Set the service state of the service.
     *
     * @param serviceState
     */
    void setServiceState(ServiceState serviceState);

    /**
     * Get if the service is locked
     *
     * @return boolean This returns if the serivce is locked. (true if yes else false)
     */
    boolean isLocked();

    /**
     * Set the service lock state.
     *
     * @param locked
     */
    void setLocked(boolean locked);

    /**
     * Set a property to the service.
     *
     * @param name
     * @param property
     */
    void addProperty(String name, Object property);

    /**
     * Remove a property from the service.
     *
     * @param name
     */
    void removeProperty(String name);

    /**
     * Get a property from the service.
     *
     * @param name
     * @return Object
     */
    Object getProperty(String name);

    /**
     * Returns the property map of the service.
     *
     * @return HasMap<String, Object>
     */
    HashMap<String, Object> getPropertyMap();

    /**
     * Get the service type of the service
     *
     * @return ServiceType this returns the service type of the service
     */
    default ServiceType getServiceType() {
        return getServiceGroup().getServiceType();
    }

    /**
     * Get if the service is online
     *
     * @return boolean This returns if the serivce is online. (true if yes else false)
     */
    default boolean isOnline() {
        return getServiceState() == ServiceState.ONLINE;
    }

    /**
     * Get if the service is preparing
     *
     * @return boolean This returns if the serivce is preparing. (true if yes else false)
     */
    default boolean isPreparing() {
        return getServiceState() == ServiceState.PREPARING;
    }

    default void copy(ITemplate template) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCopyServiceToTemplate(getServiceName(), template.getPath().toString()));
    }

    /**
     * Update every change by api to cloud.
     */
    void update();

    /**
     * Shutdown this service.
     */
    default void shutdown() {

    }

    /**
     * Force shutdown the service.
     */
    default void forceShutdown() {
    }
}
