package cloud.terium.bridge.player;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnect;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Setter;

import java.util.UUID;

@Setter
public class CloudPlayer implements ICloudPlayer {

    private UUID uniqueId;
    private String username;
    private long firstLogin;
    private long lastLogin;
    private String address;
    private ICloudService connectedService;

    public CloudPlayer(String username, UUID uniqueId, long firstLogin, long lastLogin, String address) {
        this.username = username;
        this.uniqueId = uniqueId;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.address = address;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public long getFirstLogin() {
        return firstLogin;
    }

    @Override
    public long getLastLogin() {
        return lastLogin;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public ICloudService getConnectedCloudService() {
        return connectedService;
    }

    @Override
    public void connectCloudPlayerToCloudPlayerService(ICloudPlayer iCloudPlayer) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(uniqueId, iCloudPlayer.getConnectedCloudService().getServiceName()));
    }

    @Override
    public void connectWithService(ICloudService iCloudService) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(uniqueId, iCloudService.getServiceName()));
    }
}