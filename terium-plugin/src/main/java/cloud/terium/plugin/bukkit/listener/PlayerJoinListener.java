package cloud.terium.plugin.bukkit.listener;

import cloud.terium.teriumapi.TeriumAPI;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().isLocked() && !event.getPlayer().hasPermission("terium.locked.join")) {
            event.getPlayer().kickPlayer("§cThis service is locked.");
        }

        TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        TeriumAPI.getTeriumAPI().getProvider().getThisService().update();

        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> {
            cloudPlayer.updateUsername(event.getPlayer().getName());
            cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getThisService());

            try {
                Property prop = ((GameProfile) event.getPlayer().getClass().getDeclaredMethod("getProfile").invoke(event.getPlayer())).getProperties().get("textures").iterator().next();
                cloudPlayer.updateSkinData(prop.getValue(), prop.getSignature());
            } catch (Exception ignored) {
            }

            cloudPlayer.update();
        });
    }
}