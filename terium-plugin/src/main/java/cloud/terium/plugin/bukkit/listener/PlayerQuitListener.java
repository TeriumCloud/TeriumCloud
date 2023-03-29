package cloud.terium.plugin.bukkit.listener;

import cloud.terium.teriumapi.TeriumAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(Bukkit.getOnlinePlayers().size() - 1);
        TeriumAPI.getTeriumAPI().getProvider().getThisService().update();
    }
}