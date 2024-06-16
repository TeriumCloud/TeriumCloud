package cloud.terium.plugin.bungeecord.listener;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerConnectedService;
import cloud.terium.plugin.bungeecord.TeriumBungeecordStartup;
import cloud.terium.teriumapi.TeriumAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

    @EventHandler
    public void handleServerConnected(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerConnectedService(player.getUniqueId(), event.getTarget().getName()));
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()).ifPresent(cloudPlayer -> {
            cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(event.getTarget().getName()).orElseGet(null));
            cloudPlayer.update();
        });
    }

    @EventHandler
    public void handleKickedFromServer(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.isConnected()) {
            TeriumExtension.getInstance().getFallback(player.getUniqueId()).filter(service -> !event.getKickedFrom().getName().equals("fallback")).ifPresent(service -> {
                ServerInfo registeredServer = TeriumBungeecordStartup.getInstance().getProxy().getServerInfo(service.getServiceName());
                if (registeredServer != null) {
                    if (event.getKickedFrom() != null && event.getKickedFrom().getName().equals(registeredServer.getName())) {
                        event.setCancelServer(registeredServer);
                        event.setCancelled(true);
                        event.setKickReasonComponent(event.getKickReasonComponent() != null ? event.getKickReasonComponent() : new TextComponent[]{new TextComponent("")});
                    } else {
                        event.setCancelServer(registeredServer);
                        event.setCancelled(true);
                    }
                }
            });
        }
    }

}
