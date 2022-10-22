package cloud.terium.bridge.velocity.listener;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnectedService;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.index.qual.SubstringIndexBottom;
import org.jetbrains.annotations.NotNull;

public class ServerConnectedListener {

    @Subscribe
    public void handleServerConnected(final @NotNull ServerConnectedEvent event) {
        Player player = event.getPlayer();
        CloudPlayer cloudPlayer = (CloudPlayer) TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId());

        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnectedService(player.getUniqueId(), event.getServer().getServerInfo().getName()));
        cloudPlayer.setConnectedService(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(event.getServer().getServerInfo().getName()));
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
