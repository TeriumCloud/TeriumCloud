package cloud.terium.teriumapi.player;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.UUID;

public interface ICloudPlayer {

    /**
     * Get the username from the cloud player as long
     * @return String This returns the username of the cloud player.
     */
    String getUsername();

    /**
     * Get the UUID from the cloud player as long
     * @return String This returns the UUID of the cloud player.
     */
    UUID getUniqueId();

    /**
     * Get the first login from the cloud player as long
     * @return long This returns the first login of the cloud player.
     */
    long getFirstLogin();

    /**
     * Get the last login from the cloud player as long
     * @return long This returns the last login of the cloud player.
     */
    long getLastLogin();

    /**
     * Get the IP Address from the cloud player
     * @return String This returns the address of the cloud player.
     */
    String getAddress();

    /**
     * Get the server from the cloud player
     * @return ICloudService This returns the ICloudService the cloud player is connected with.
     */
    ICloudService getConnectedCloudService();

    /**
     * Connect a cloud player to the service from a other cloud player
     * @param cloudPlayer This is the cloudplayer the player should be connected with.
     */
    void connectCloudPlayerToCloudPlayerService(ICloudPlayer cloudPlayer);

    /**
     * Connect a cloud player with the service in param
     * @param cloudService This is the cloud service the cloud player should be connected with.
     */
    void connectWithService(ICloudService cloudService);
}
