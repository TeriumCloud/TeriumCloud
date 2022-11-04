package cloud.terium.bridge.bukkit.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.BridgeBukkitStartup;
import cloud.terium.networking.packets.PacketPlayOutServiceOnlinePlayersUpdatePacket;
import cloud.terium.teriumapi.TeriumAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceOnlinePlayersUpdatePacket(TeriumBridge.getInstance().getThisName(), BridgeBukkitStartup.getInstance().getServer().getOnlinePlayers().size()));

        if(TeriumAPI.getTeriumAPI().getProvider().getThisService().isLocked()) {
            event.getPlayer().kick(Component.text("§cThis service is locked!"));
        }
    }
}