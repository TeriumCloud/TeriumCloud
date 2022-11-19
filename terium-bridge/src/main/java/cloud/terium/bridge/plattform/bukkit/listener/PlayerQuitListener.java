package cloud.terium.bridge.plattform.bukkit.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.plattform.bukkit.BridgeBukkitStartup;
import cloud.terium.networking.packets.service.PacketPlayOutServiceOnlinePlayersUpdatePacket;
import cloud.terium.teriumapi.TeriumAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TeriumBridge.getInstance().getCloudPlayerProvider().updateCloudPlayer(TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()));
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceOnlinePlayersUpdatePacket(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName(), BridgeBukkitStartup.getInstance().getServer().getOnlinePlayers().size() - 1));
    }
}