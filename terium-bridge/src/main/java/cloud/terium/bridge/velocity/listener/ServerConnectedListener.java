package cloud.terium.bridge.velocity.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.teriumapi.service.ICloudService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class ServerConnectedListener {

    @Subscribe
    public void handleServerConnected(final @NotNull ServerConnectedEvent event) {
        Player player = event.getPlayer();
        CloudPlayer cloudPlayer = TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId());

        cloudPlayer.setConnectedService(TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(event.getServer().getServerInfo().getName()));
        player.sendPlayerListHeaderAndFooter(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("tablist_header").getAsString().replace("%server%", cloudPlayer.getConnectedService().getServiceName()).replace("%group_title%", cloudPlayer.getConnectedService().getServiceGroup().getGroupTitle())),
                MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("tablist_footer").getAsString().replace("%server%", cloudPlayer.getConnectedService().getServiceName()).replace("%group_title%", cloudPlayer.getConnectedService().getServiceGroup().getGroupTitle())));
    }

    @Subscribe
    public void handleServerConnected(final @NotNull ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        ICloudService minecraftService = TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().stream().filter(ICloudService::isOnline).toList().size() > 0 ? TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().stream().filter(ICloudService::isOnline).toList().get(0) : null;

        if (event.getResult().getServer().get().getServerInfo().getName().equals("fallback") && minecraftService != null)
            player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(minecraftService.getServiceName()).get()).connect();
    }

    @Subscribe
    public void handleKickedFromServer(final @NotNull KickedFromServerEvent event) {
        if (event.getPlayer().isActive()) {
            TeriumBridge.getInstance().getFallback(event.getPlayer()).flatMap(service -> BridgeVelocityStartup.getInstance().getProxyServer().getServer(service.getServiceName()))
                    .ifPresent(registeredServer -> {
                        if (event.getServer() != null && event.getServer().getServerInfo().getName().equals(registeredServer.getServerInfo().getName())) {
                            event.setResult(KickedFromServerEvent.Notify.create(event.getServerKickReason().orElse(Component.empty())));
                        } else {
                            event.setResult(KickedFromServerEvent.RedirectPlayer.create(registeredServer));
                        }
                    });
        }
    }
}
