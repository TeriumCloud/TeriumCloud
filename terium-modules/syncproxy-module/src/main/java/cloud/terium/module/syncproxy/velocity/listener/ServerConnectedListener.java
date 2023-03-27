package cloud.terium.module.syncproxy.velocity.listener;

import cloud.terium.module.syncproxy.velocity.SyncproxyVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ServerConnectedListener {

    @Subscribe
    public void handleServerConnected(final @NotNull ServerConnectedEvent event) {
        Player player = event.getPlayer();
        Optional<ICloudPlayer> optionalICloudPlayer = TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId());

        optionalICloudPlayer.ifPresent(cloudPlayer -> SyncproxyVelocityStartup.getInstance().getProxyServer().getScheduler().buildTask(SyncproxyVelocityStartup.getInstance(), () -> SyncproxyVelocityStartup.getInstance().getProxyServer().getAllPlayers().forEach(players -> player.sendPlayerListHeaderAndFooter(
                MiniMessage.miniMessage().deserialize(SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("tablist_header").getAsString()
                        .replace("%server%", cloudPlayer.getConnectedCloudService().get().getServiceName())
                        .replace("%group_title%", cloudPlayer.getConnectedCloudService().get().getServiceGroup().getGroupTitle())),
                MiniMessage.miniMessage().deserialize(SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("tablist_footer").getAsString()
                        .replace("%server%", cloudPlayer.getConnectedCloudService().get().getServiceName())
                        .replace("%group_title%", cloudPlayer.getConnectedCloudService().get().getServiceGroup().getGroupTitle()))))).delay(1, TimeUnit.SECONDS).schedule());
    }
}