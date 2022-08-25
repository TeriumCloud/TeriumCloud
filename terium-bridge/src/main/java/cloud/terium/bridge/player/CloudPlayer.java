package cloud.terium.bridge.player;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnect;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CloudPlayer {

    private UUID uniqueId;
    private String username;
    private String title;
    private CloudRank rank;
    private ICloudService connectedService;

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