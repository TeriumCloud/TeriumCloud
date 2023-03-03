package cloud.terium.plugin.velocity.listener;

import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerJoin;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
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

import java.util.Optional;
import java.util.UUID;

public class LoginListener {

    @Subscribe
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();
        Optional<ICloudPlayer> iCloudPlayer = TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId());
        iCloudPlayer.ifPresentOrElse(cloudPlayer -> {

        }, () -> {

        });
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(player.getUniqueId()));

        if (!TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllLobbyServices().isEmpty()) {
            Optional<ICloudService> minecraftService = TeriumPlugin.getInstance().getFallback(player);

            if (minecraftService.isPresent()) {
                if(minecraftService.get().isLocked()) {
                    player.disconnect(Component.text("§cThis service is locked!"));
                    return;
                }

                player.createConnectionRequest(TeriumVelocityStartup.getInstance().getProxyServer().getServer(minecraftService.get().getServiceName()).get()).connect();
                iCloudPlayer.ifPresent(cloudPlayer -> cloudPlayer.updateConnectedService(minecraftService.get()));
                TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()).ifPresent(cloudService -> {
                    cloudService.setOnlinePlayers(TeriumVelocityStartup.getInstance().getProxyServer().getPlayerCount() + 1);
                    cloudService.update();
                });
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
        event.setInitialServer(TeriumPlugin.getInstance().getFallback(event.getPlayer())
                .flatMap(service -> TeriumVelocityStartup.getInstance().getProxyServer().getServer(service.getServiceName()))
                .orElse(null));
    }

    @Subscribe
    public void handleDisconnect(DisconnectEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> {
            cloudPlayer.updateUsername(event.getPlayer().getUsername());
            cloudPlayer.updateAddress(event.getPlayer().getRemoteAddress().getHostName());
            cloudPlayer.updateConnectedService(null);
            cloudPlayer.update();
        });

        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()).ifPresent(cloudService -> {
            cloudService.setOnlinePlayers(TeriumVelocityStartup.getInstance().getProxyServer().getPlayerCount() - 1);
            cloudService.update();
        });
    }
}