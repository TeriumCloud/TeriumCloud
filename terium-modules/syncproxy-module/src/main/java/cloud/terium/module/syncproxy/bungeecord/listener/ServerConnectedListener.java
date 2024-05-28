package cloud.terium.module.syncproxy.bungeecord.listener;

import cloud.terium.module.syncproxy.bungeecord.SyncproxyBungeecordStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ServerConnectedListener implements Listener {

    @EventHandler
    public void handleServerConnected(final ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Optional<ICloudPlayer> optionalICloudPlayer = TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId());

        optionalICloudPlayer.ifPresent(cloudPlayer -> {
            ProxyServer.getInstance().getScheduler().schedule(SyncproxyBungeecordStartup.getInstance(), () -> {
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    String header = SyncproxyBungeecordStartup.getInstance().getConfigManager().getJson().get("tablist_header").getAsString()
                            .replace("%server%", cloudPlayer.getConnectedCloudService().get().getServiceName())
                            .replace("%group_title%", cloudPlayer.getConnectedCloudService().get().getServiceGroup().getGroupTitle())
                            .replace("%local_players%", String.valueOf(cloudPlayer.getConnectedCloudService().get().getOnlinePlayers()))
                            .replace("%online_players%", String.valueOf(TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers()))
                            .replace("%maximum_players%", String.valueOf(TeriumAPI.getTeriumAPI().getProvider().getThisService().getMaxPlayers()));
                    String footer = SyncproxyBungeecordStartup.getInstance().getConfigManager().getJson().get("tablist_footer").getAsString()
                            .replace("%server%", cloudPlayer.getConnectedCloudService().get().getServiceName())
                            .replace("%group_title%", cloudPlayer.getConnectedCloudService().get().getServiceGroup().getGroupTitle())
                            .replace("%local_players%", String.valueOf(cloudPlayer.getConnectedCloudService().get().getOnlinePlayers()))
                            .replace("%online_players%", String.valueOf(TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers()))
                            .replace("%maximum_players%", String.valueOf(TeriumAPI.getTeriumAPI().getProvider().getThisService().getMaxPlayers()));


                    BaseComponent[] headerComponents = BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(header));
                    BaseComponent[] footerComponents = BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(footer));

                    player.setTabHeader(headerComponents, footerComponents);
                }
            }, 1, TimeUnit.SECONDS);
        });
    }
}
