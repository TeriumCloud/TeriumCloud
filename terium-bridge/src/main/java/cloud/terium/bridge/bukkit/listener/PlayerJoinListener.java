package cloud.terium.bridge.bukkit.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
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
    }
}
