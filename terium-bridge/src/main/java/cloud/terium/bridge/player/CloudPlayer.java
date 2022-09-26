package cloud.terium.bridge.player;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnect;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CloudPlayer implements ICloudPlayer {

    private UUID uniqueId;
    private String username;
    private String title;
    private CloudRank rank;
    private ICloudService connectedService;

    /*
     * TODO: CODE CLOUDPLAYER! NOW!
     */
    @Override
    public long firstLogin() {
        return 0;
    }

    @Override
    public long lastLogin() {
        return 0;
    }

    @Override
    public String getAddress() {
        return null;
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

    public boolean hasRankOrHigher(CloudRank cloudRank) {
        return rank.getPower() >= cloudRank.getPower();
    }

    public boolean isAdmin() {
        return rank.equals(CloudRank.Admin);
    }
}