package cloud.terium.plugin.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler
    public void handle(PlayerCommandPreprocessEvent event){
        String message = event.getMessage();
        if (message.equalsIgnoreCase("/rl") ||
                message.equalsIgnoreCase("/reload") ||
                message.equalsIgnoreCase("/rl confirm") ||
                message.equalsIgnoreCase("/reload confirm")){

            if (event.getPlayer().hasPermission("bukkit.command.reload")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cterium-cloud services can't be reloaded.");
            }
        }
    }
}
