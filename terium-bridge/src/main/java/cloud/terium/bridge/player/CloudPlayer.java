package cloud.terium.bridge.player;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnect;
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
    private MinecraftService connectedService;

    public void connectWithService(MinecraftService minecraftService) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnect(username, uniqueId, minecraftService.serviceName()));
    }

    public boolean hasRankOrHigher(CloudRank cloudRank) {
        return rank.getPower() >= cloudRank.getPower();
    }

    public boolean isAdmin() {
        return rank.equals(CloudRank.Admin);
    }
}