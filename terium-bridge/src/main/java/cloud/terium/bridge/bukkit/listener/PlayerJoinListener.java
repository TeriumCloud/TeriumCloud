package cloud.terium.bridge.bukkit.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.BridgeBukkitStartup;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.networking.packets.PacketPlayOutServiceOnlinePlayersUpdatePacket;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceOnlinePlayersUpdatePacket(TeriumBridge.getInstance().getThisName(), BridgeBukkitStartup.getInstance().getServer().getOnlinePlayers().size()));

        if(TeriumBridge.getInstance().getThisService().isLocked()) {
            event.getPlayer().kick(Component.text("§cThis service is locked!"));
        }
    }
}