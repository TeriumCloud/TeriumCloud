package cloud.terium.module.syncproxy.velocity.listener;

import cloud.terium.module.syncproxy.velocity.SyncproxyVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ProxyPingListener {

    @Subscribe
    public void handleProxyPing(ProxyPingEvent event) {
        ServerPing.Builder builder = event.getPing().asBuilder();

        builder.description(MiniMessage.miniMessage().deserialize(SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("motd.line1").getAsString() + "\n" +
                SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("motd.line2").getAsString()));
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().isMaintenance())
            builder.description(MiniMessage.miniMessage().deserialize(SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("motd.maintenance.line1").getAsString() + "\n" +
                    SyncproxyVelocityStartup.getInstance().getConfigManager().getJson().get("motd.maintenance.line2").getAsString()));

        event.setPing(builder.build());
    }
}