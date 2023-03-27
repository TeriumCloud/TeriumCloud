package cloud.terium.module.syncproxy.listener;

import cloud.terium.module.syncproxy.manager.ConfigManager;
import cloud.terium.module.syncproxy.velocity.SyncproxyVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.config.ReloadConfigEvent;
import cloud.terium.teriumapi.events.player.CloudPlayerJoinEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CloudListener implements Listener {

    @Subscribe
    public void handleCloudPlayerJoin(CloudPlayerJoinEvent event) {
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().isMaintenance()) {
            if (!SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("whitelist").getAsJsonArray().toString().contains(event.getCloudPlayer().getUsername())) {
                SyncproxyVelocityStartup.getInstance().getProxyServer().getPlayer(event.getCloudPlayer().getUniqueId()).ifPresent(player -> player.disconnect(MiniMessage.miniMessage().deserialize(SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("maintenance.message").getAsString())));
            }
        }

        if (SyncproxyVelocityStartup.getInstance().getProxyServer().getPlayerCount() >= TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().getMaxPlayers() &&
                SyncproxyVelocityStartup.getInstance().getProxyServer().getPlayer(event.getCloudPlayer().getUniqueId()).orElseGet(null).hasPermission("terium.full_kick")) {
            SyncproxyVelocityStartup.getInstance().getProxyServer().getPlayer(event.getCloudPlayer().getUniqueId()).ifPresent(player ->
                    player.disconnect(MiniMessage.miniMessage().deserialize(SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("full_kick.message").getAsString())));
        }
    }

    @Subscribe
    public void handleReloadConfig(ReloadConfigEvent event) {
        SyncproxyVelocityStartup.getInstance().setConfigManager(new ConfigManager());
    }
}