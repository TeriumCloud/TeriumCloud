package cloud.terium.bridge.velocity.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerJoin;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerQuit;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.UUID;

public class LoginListener {

    @Subscribe
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();
        CloudPlayer cloudPlayer = TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId());
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(player.getUsername(), player.getUniqueId()));

        if (!TeriumBridge.getInstance().getServiceManager().getLobbyServices().isEmpty()) {
            MinecraftService minecraftService = TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().size() > 0 ? TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().get(0) : null;

            if (minecraftService != null) {
                player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(TeriumBridge.getInstance().getServiceManager().getLobbyServices().get(0).serviceName()).get()).connect();
                cloudPlayer.setConnectedService(minecraftService);
            } else {
                player.disconnect(Component.text("§cThe terium-cloud is starting a lobby service. Please wait a moment."));
            }
        } else {
            player.disconnect(Component.text("§cThe terium-cloud can't find a lobby service. Please try again later or contact an admin."));
            return;
        }

        if (player.getUniqueId().equals(UUID.fromString("c1685728-72d6-4dbe-8899-28c4aa3cb93c"))) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("This server is running <gradient:#245dec:#00d4ff>Terium</gradient><white>."));
        }

        if (TeriumBridge.getInstance().getThisService().defaultServiceGroup().maintenance()) {
            if (!player.hasPermission("terium.maintenancejoin")) {
                player.disconnect(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("maintenance.message").getAsString()));
            }
        }

        if (BridgeVelocityStartup.getInstance().getProxyServer().getPlayerCount() >= TeriumBridge.getInstance().getThisService().getDefaultServiceGroup().maximumPlayers() && player.hasPermission("terium.full_kick")) {
            player.disconnect(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("full_kick.message").getAsString()));
        }
    }

    @Subscribe
    public void handleDisconnect(DisconnectEvent event) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerQuit(event.getPlayer().getUsername(), event.getPlayer().getUniqueId()));
        TeriumBridge.getInstance().getCloudPlayerManager().updateCloudPlayer(TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(event.getPlayer().getUsername(), event.getPlayer().getUniqueId()), true);
    }
}