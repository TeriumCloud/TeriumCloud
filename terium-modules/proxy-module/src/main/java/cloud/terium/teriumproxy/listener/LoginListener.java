package cloud.terium.teriumproxy.listener;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumproxy.TeriumProxy;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LoginListener {

    @Subscribe
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();

        if (TeriumAPI.getTeriumAPI().getThisService().getServiceGroup().isMaintenance()) {
            if (!TeriumProxy.getInstance().getConfigManager().getJson().get("whitelist").getAsJsonArray().toString().contains(player.getUsername())) {
                player.disconnect(MiniMessage.miniMessage().deserialize(TeriumProxy.getInstance().getConfigManager().getString("maintenance.message")));
            }
        }

        if (TeriumProxy.getInstance().getProxyServer().getPlayerCount() >= TeriumAPI.getTeriumAPI().getThisService().getServiceGroup().getMaximumPlayers() && player.hasPermission("terium.full_kick")) {
            player.disconnect(MiniMessage.miniMessage().deserialize(TeriumProxy.getInstance().getConfigManager().getString("full_kick.message")));
        }
    }
}