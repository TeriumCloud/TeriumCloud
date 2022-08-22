package cloud.terium.bridge.bukkit.listener;

import cloud.terium.bridge.TeriumBridge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TeriumBridge.getInstance().getCloudPlayerManager().updateCloudPlayer(TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getName(), player.getUniqueId()), true);
    }
}