package cloud.terium.teriumapi.service;

import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

public interface ICloudService {

    /**
     * Get the service name of a serivce
     *
     * @return String This returns the service name as String.
     */
    String getServiceName();

    /**
     * Get the service id of a service
     *
     * @return int This returns the service of the service.
     */
    int getServiceId();

    /**
     * Get the port of the currrent service
     *
     * @return int This returns the port of the service.
     */
    int getPort();

    /**
     * Get the template of the currrent service
     *
     * @return int This returns the port of the service.
     */
    ITemplate getTemplate();

    /**
     * Get the maximal players who can join on the service
     *
     * @return int This returns the maximal player who can join as int.
     */
    default int getMaxPlayers() {
        return getServiceGroup().getMaximumPlayers();
    }

    /**
     * Get the online players on the service
     *
     * @return int This returns the online players as int.
     */
    int getOnlinePlayers();

    /**
     * Get the used memory from the service
     *
     * @return int This returns the used memory from the service as long.
     */
    long getUsedMemory();

    /**
     * Update every change by api to cloud.
     */
    void update();

    /**
     * Get the max memory of the service
     *
     * @return int This returns the maximal memory of the service as int.
     */
    default int getMaxMemory() {
        return getServiceGroup().getMemory();
    }

    /**
     * Get the ICloudServiceGroup of the serivce
     *
     * @return ICloudServiceGroup This returns the ICloudServiceGroup of the service.
     */
    ICloudServiceGroup getServiceGroup();

    /**
     * Get the ServiceState of the service
     *
     * @return CloudServiceGroup This returns the current service state.
     */
    CloudServiceState getServiceState();

    /**
     * Get if the service is locked
     *
     * @return boolean This returns if the serivce is locked. (true if yes else false)
     */
    boolean isLocked();

    /**
     * Get the service type of the service
     *
     * @return CloudServiceType this returns the service type of the service
     */
    default CloudServiceType getServiceType() {
        return getServiceGroup().getServiceType();
    }

    /**
     * Get if the service is online
     *
     * @return boolean This returns if the serivce is online. (true if yes else false)
     */
    default boolean isOnline() {
        return getServiceState() == CloudServiceState.ONLINE;
    }

    /**
     * Get if the service is preparing
     *
     * @return boolean This returns if the serivce is preparing. (true if yes else false)
     */
    default boolean isPreparing() {
        return getServiceState() == CloudServiceState.PREPARING;
    }

    /**
     * Set the service lock state.
     *
     * @param locked
     */
    void setLocked(boolean locked);

    /**
     * Set the online players of the service.
     *
     * @param onlinePlayers
     */
    void setOnlinePlayers(int onlinePlayers);

    /**
     * Set the used memory of the service.
     *
     * @param usedMemory
     */
    void setUsedMemory(long usedMemory);

    /**
     * Set the service state of the service.
     *
     * @param serviceState
     */
    void setServiceState(CloudServiceState serviceState);

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
