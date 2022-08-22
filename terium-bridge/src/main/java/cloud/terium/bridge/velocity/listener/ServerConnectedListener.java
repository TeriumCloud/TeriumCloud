package cloud.terium.bridge.velocity.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.cloudsystem.service.MinecraftService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ServerConnectedListener {

    @Subscribe
    public void handleServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        CloudPlayer cloudPlayer = TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId());

        cloudPlayer.setConnectedService(TeriumBridge.getInstance().getServiceManager().getServiceByName(event.getServer().getServerInfo().getName()));
        player.sendPlayerListHeaderAndFooter(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("tablist_header").getAsString().replace("%server%", cloudPlayer.getConnectedService().serviceName()).replace("%group_title%", cloudPlayer.getConnectedService().getDefaultServiceGroup().groupTitle())),
                MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("tablist_footer").getAsString().replace("%server%", cloudPlayer.getConnectedService().serviceName()).replace("%group_title%", cloudPlayer.getConnectedService().getDefaultServiceGroup().groupTitle())));
    }

    @Subscribe
    public void handleServerConnected(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        MinecraftService minecraftService = TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().size() > 0 ? TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().get(0) : null;

        player.sendMessage(MiniMessage.miniMessage().deserialize(minecraftService + " / " + event.getResult().getServer().get().getServerInfo().getName()));

        if(event.getResult().getServer().get().getServerInfo().getName().equals("fallback") && minecraftService != null)
            player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(minecraftService.serviceName()).get()).connect();
    }

    @Subscribe
    public void handleServerConnected(KickedFromServerEvent event) {
        Player player = event.getPlayer();
        MinecraftService minecraftService = TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().size() > 0 ? TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().get(0) : null;

        player.sendMessage(MiniMessage.miniMessage().deserialize("KICKED SERVER: " + minecraftService + " / "));

        if(minecraftService != null)
            player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(minecraftService.serviceName()).get()).connect();
    }
}
