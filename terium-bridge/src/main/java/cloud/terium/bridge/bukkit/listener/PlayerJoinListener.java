package cloud.terium.bridge.bukkit.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.BridgeBukkitStartup;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.networking.packets.PacketPlayOutServiceOnlinePlayersUpdatePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CloudPlayer cloudPlayer = TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getName(), player.getUniqueId());
        cloudPlayer.setConnectedService(TeriumBridge.getInstance().getThisService());
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceOnlinePlayersUpdatePacket(TeriumBridge.getInstance().getThisName(), BridgeBukkitStartup.getInstance().getServer().getOnlinePlayers().size()));
    }
}
