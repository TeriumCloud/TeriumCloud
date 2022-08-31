package cloud.terium.teriumapi.player;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.UUID;

public interface ICloudPlayer {

    String getUsername();

    UUID getUniqueId();

    long firstLogin();

    long lastLogin();

    /*
     * Get the IP Address from the cloud player
     */
    String getAddress();

    /*
     * Get the server from the cloud player
     */
    ICloudService getConnectedCloudService();

    /*
     * Connect a cloud player to the service from a other cloud player
     */
    void connectCloudPlayerToCloudPlayerService(ICloudPlayer cloudPlayer);

    /*
     * Connect a cloud player with the service in param
     */
    void connectWithService(ICloudService cloudService);
}
