package cloud.terium.teriumproxy.listener;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumproxy.TeriumProxy;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ProxyPingListener {

    @Subscribe
    public void handleProxyPing(ProxyPingEvent event) {
        ServerPing.Builder builder = event.getPing().asBuilder();

        builder.description(MiniMessage.miniMessage().deserialize(TeriumProxy.getInstance().getConfigManager().getString("motd.line1") + "\n" +
                TeriumProxy.getInstance().getConfigManager().getString("motd.line2")));
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().isMaintenance())
            builder.description(MiniMessage.miniMessage().deserialize(TeriumProxy.getInstance().getConfigManager().getString("motd.maintenance.line1") + "\n" +
                    TeriumProxy.getInstance().getConfigManager().getString("motd.maintenance.line2")));

        event.setPing(builder.build());
    }
}