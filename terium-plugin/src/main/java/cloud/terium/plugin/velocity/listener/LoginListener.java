package cloud.terium.plugin.velocity.listener;

import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerJoin;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerQuit;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerRegister;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import com.velocitypowered.api.event.PostOrder;
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

    private void accept(ICloudPlayer cloudPlayer) {
    }

    @Subscribe
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()).ifPresentOrElse(this::accept, () -> {
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerRegister(event.getPlayer().getUsername(), event.getPlayer().getUniqueId(), event.getPlayer().getRemoteAddress(), "", "", TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()));
        });

        if (!TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllLobbyServices().isEmpty()) {
            Optional<ICloudService> minecraftService = TeriumPlugin.getInstance().getFallback(player);

            if (minecraftService.isPresent()) {
                if (minecraftService.get().isLocked() && !player.hasPermission("terium.locked.join")) {
                    player.disconnect(Component.text("§cThis service is locked!"));
                    return;
                }

                TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()).ifPresent(cloudService -> {
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
            player.sendMessage(MiniMessage.miniMessage().deserialize("This server is running <gradient:#245dec:#00d4ff>terium-cloud</gradient><white> v" + TeriumAPI.getTeriumAPI().getProvider().getVersion() + "."));
        }
    }

    @Subscribe
    public void handlePlayerChooseInitialServer(final @NotNull PlayerChooseInitialServerEvent event) {
        Optional<ICloudService> cloudService = TeriumPlugin.getInstance().getFallback(event.getPlayer());

        event.setInitialServer(cloudService
                .flatMap(service -> TeriumVelocityStartup.getInstance().getProxyServer().getServer(service.getServiceName()))
                .orElse(null));

        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> cloudPlayer.updateConnectedService(cloudService.orElseGet(null)));
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(event.getPlayer().getUniqueId()));
    }

    @Subscribe(order = PostOrder.LAST)
    public void handleDisconnect(DisconnectEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> {
            cloudPlayer.updateUsername(event.getPlayer().getUsername());
            cloudPlayer.updateAddress(event.getPlayer().getRemoteAddress());
            cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getThisService());
            cloudPlayer.update();
        });

        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerQuit(event.getPlayer().getUniqueId()));
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> {
            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().remove(cloudPlayer);
        });

        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()).ifPresent(cloudService -> {
            cloudService.setOnlinePlayers(TeriumVelocityStartup.getInstance().getProxyServer().getPlayerCount());
            cloudService.update();
        });
    }
}