package cloud.terium.bridge.player;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnect;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
public class CloudPlayer implements ICloudPlayer {

    private UUID uniqueId;
    private String username;
    private long firstLogin;
    private long lastLogin;
    private String addresse;
    private ICloudService connectedService;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public long firstLogin() {
        return firstLogin;
    }

    @Override
    public long lastLogin() {
        return lastLogin;
    }

    @Override
    public String getAddress() {
        return addresse;
    }

    @Override
    public ICloudService getConnectedCloudService() {
        return connectedService;
    }

    @Override
    public void connectCloudPlayerToCloudPlayerService(ICloudPlayer iCloudPlayer) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(username, uniqueId, iCloudPlayer.getConnectedCloudService().getServiceName()));
    }

    @Override
    public void connectWithService(ICloudService iCloudService) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(username, uniqueId, iCloudService.getServiceName()));
    }
}