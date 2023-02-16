package cloud.terium.teriumapi.player;

import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerConnect;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public interface ICloudPlayer extends Serializable {

    /**
     * Get the username from the cloud player as long
     *
     * @return String This returns the username of the cloud player.
     */
    String getUsername();

    /**
     * Get the UUID from the cloud player as long
     *
     * @return String This returns the UUID of the cloud player.
     */
    UUID getUniqueId();

    /**
     * Get the IP Address from the cloud player
     *
     * @return String This returns the address of the cloud player.
     */
    String getAddress();

    /**
     * Get the server from the cloud player
     *
     * @return ICloudService This returns the ICloudService the cloud player is connected with.
     */
    Optional<ICloudService> getConnectedCloudService();

    /**
     * Update the username of the cloud player
     *
     * @param username
     */
    void updateUsername(String username);

    /**
     * Update the uniqueId of the cloud player
     *
     * @param uniqueId
     */
    void updateUniqueId(UUID uniqueId);

    /**
     * Update the address of the cloud palyer
     *
     * @param address
     */
    void updateAddress(String address);

    /**
     * Update the connected cloud service of the cloud player
     *
     * @param cloudService
     */
    void updateConnectedService(ICloudService cloudService);

    /**
     * Connect a cloud player to the service from a other cloud player
     *
     * @param cloudPlayer This is the cloudplayer the player should be connected with.
     */
    default void connectCloudPlayerToCloudPlayerService(ICloudPlayer cloudPlayer) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(getUniqueId(), cloudPlayer.getConnectedCloudService().orElseGet(null).getServiceName()));
    }

    /**
     * Connect a cloud player with the service in param
     *
     * @param cloudService This is the cloud service the cloud player should be connected with.
     */
    default void connectWithService(ICloudService cloudService) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(getUniqueId(), cloudService.getServiceName()));
    }
}
