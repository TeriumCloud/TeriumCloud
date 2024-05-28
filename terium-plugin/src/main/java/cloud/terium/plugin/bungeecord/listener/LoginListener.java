package cloud.terium.plugin.bungeecord.listener;

import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerJoin;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerQuit;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerRegister;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.bungeecord.TeriumBungeecordStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Optional;
import java.util.UUID;

public class LoginListener implements Listener {

    private void accept(ICloudPlayer cloudPlayer) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()).ifPresentOrElse(this::accept, () -> {
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerRegister(player.getName(), player.getUniqueId(), player.getAddress(), "", "", TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()));
        });

        if (!TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllLobbyServices().isEmpty()) {
            Optional<ICloudService> minecraftService = TeriumPlugin.getInstance().getWaterfallFallback(player);

            if (minecraftService.isPresent()) {
                if (minecraftService.get().isLocked() && !player.hasPermission("terium.locked.join")) {
                    player.disconnect(ChatColor.RED + "This service is locked!");
                    return;
                }

                TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()).ifPresent(cloudService -> {
                    cloudService.setOnlinePlayers(TeriumBungeecordStartup.getInstance().getProxy().getOnlineCount() + 1);
                    cloudService.update();
                });
            } else {
                player.disconnect(ChatColor.RED + "The terium-cloud is starting a lobby service. Please wait a moment.");
            }
        } else {
            player.disconnect(ChatColor.RED + "The terium-cloud can't find a lobby service. Please try again later or contact an admin.");
            return;
        }

        if (player.getUniqueId().equals(UUID.fromString("c1685728-72d6-4dbe-8899-28c4aa3cb93c"))) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "This server is running &bterium-cloud&f v" + TeriumAPI.getTeriumAPI().getProvider().getVersion() + "."));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerChooseInitialServer(ServerConnectEvent event) {
        Optional<ICloudService> cloudService = TeriumPlugin.getInstance().getWaterfallFallback(event.getPlayer());

        cloudService.ifPresentOrElse(service -> event.setTarget(TeriumBungeecordStartup.getInstance().getProxy().getServerInfo(service.getServiceName())), () -> {
            event.getPlayer().sendMessage(new TextComponent("<red>No fallback server found."));
        });

        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> cloudPlayer.updateConnectedService(cloudService.orElseGet(null)));
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerJoin(event.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()).ifPresent(cloudPlayer -> {
            cloudPlayer.updateUsername(player.getName());
            cloudPlayer.updateAddress(player.getAddress());
            cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getThisService());
            cloudPlayer.update();
        });

        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerQuit(player.getUniqueId()));
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()).ifPresent(cloudPlayer -> {
            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().remove(cloudPlayer);
        });

        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceName()).ifPresent(cloudService -> {
            cloudService.setOnlinePlayers(TeriumBungeecordStartup.getInstance().getProxy().getOnlineCount());
            cloudService.update();
        });
    }
}
