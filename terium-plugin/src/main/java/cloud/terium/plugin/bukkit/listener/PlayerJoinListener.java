package cloud.terium.plugin.bukkit.listener;

import cloud.terium.teriumapi.TeriumAPI;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(TeriumAPI.getTeriumAPI().getProvider().getThisService().getOnlinePlayers() + 1);
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