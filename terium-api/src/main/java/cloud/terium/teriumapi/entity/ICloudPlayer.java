package cloud.terium.teriumapi.entity;

import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerConnect;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerUpdate;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;

import java.io.Serializable;
import java.net.InetSocketAddress;
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
    InetSocketAddress getAddress();

    /**
     * Get the skin value from the cloud player as long
     *
     * @return String This returns the skin value of the cloud player.
     */
    String getSkinValue();

    /**
     * Get the skin signature from the cloud player
     *
     * @return String This returns the skin signature of the cloud player.
     */
    String getSkinSignature();

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
     * Update the address of the cloud palyer
     *
     * @param address
     */
    void updateAddress(InetSocketAddress address);

    /**
     * Update the connected cloud service of the cloud player
     *
     * @param cloudService
     */
    void updateConnectedService(ICloudService cloudService);

    /**
     * Update the skin data of the cloud player
     *
     * @param value
     * @param signature
     */
    void updateSkinData(String value, String signature);

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

    /**
     * Send all updated informations to all cloud services, and cloud instances.
     */
    default void update() {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerUpdate(getUniqueId(), getUsername(), getAddress(), getSkinValue(), getSkinSignature(), getConnectedCloudService().orElseGet(null).getServiceName()));
    }
}
