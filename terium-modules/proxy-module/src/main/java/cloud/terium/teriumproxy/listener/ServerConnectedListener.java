package cloud.terium.teriumproxy.listener;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumproxy.TeriumProxy;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ServerConnectedListener {

    @Subscribe
    public void handleServerConnected(final @NotNull ServerConnectedEvent event) {
        Player player = event.getPlayer();
        ICloudPlayer cloudPlayer = TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId());

        TeriumProxy.getInstance().getProxyServer().getScheduler().buildTask(TeriumProxy.getInstance(), () -> TeriumProxy.getInstance().getProxyServer().getAllPlayers().forEach(players -> player.sendPlayerListHeaderAndFooter(
                MiniMessage.miniMessage().deserialize(TeriumProxy.getInstance().getConfigManager().getString("tablist_header")
                        .replace("%server%", cloudPlayer.getConnectedCloudService().getServiceName())
                        .replace("%group_title%", cloudPlayer.getConnectedCloudService().getServiceGroup().getGroupTitle())),
                MiniMessage.miniMessage().deserialize(TeriumProxy.getInstance().getConfigManager().getString("tablist_footer")
                        .replace("%server%", cloudPlayer.getConnectedCloudService().getServiceName())
                        .replace("%group_title%", cloudPlayer.getConnectedCloudService().getServiceGroup().getGroupTitle()))))).delay(1, TimeUnit.SECONDS).schedule();
    }
}