package cloud.terium.bridge.velocity.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerJoin;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerQuit;
import cloud.terium.networking.packets.PacketPlayOutServiceOnlinePlayersUpdatePacket;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LoginListener {

    @Subscribe
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();
        CloudPlayer cloudPlayer = (CloudPlayer) TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUniqueId());
        if(cloudPlayer == null) {
            TeriumBridge.getInstance().getCloudPlayerManager().createPlayerJson(player);
            cloudPlayer = (CloudPlayer) TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUniqueId());
        }
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(player.getUsername(), player.getUniqueId()));

        if (!TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().isEmpty()) {
            ICloudService minecraftService = TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().stream().filter(ICloudService::isOnline).toList().size() > 0 ? TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().stream().filter(ICloudService::isOnline).toList().get(0) : null;

            if (minecraftService != null) {
                if(minecraftService.isLocked()) {
                    player.disconnect(Component.text("§cThis service is locked!"));
                    return;
                }

                player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().get(0).getServiceName()).get()).connect();
                cloudPlayer.setConnectedService(minecraftService);
                TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceOnlinePlayersUpdatePacket(TeriumBridge.getInstance().getThisName(), BridgeVelocityStartup.getInstance().getProxyServer().getPlayerCount() + 1));
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
    }

    @Subscribe
    public void handle(final @NotNull PlayerChooseInitialServerEvent event) {
        event.setInitialServer(TeriumBridge.getInstance().getFallback(event.getPlayer())
                .flatMap(service -> BridgeVelocityStartup.getInstance().getProxyServer().getServer(service.getServiceName()))
                .orElse(null));
    }

    @Subscribe
    public void handleDisconnect(DisconnectEvent event) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerQuit(event.getPlayer().getUsername(), event.getPlayer().getUniqueId()));
        TeriumBridge.getInstance().getCloudPlayerManager().updateCloudPlayer(TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(event.getPlayer().getUsername()));
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceOnlinePlayersUpdatePacket(TeriumBridge.getInstance().getThisName(), BridgeVelocityStartup.getInstance().getProxyServer().getPlayerCount()));
    }
}