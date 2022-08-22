package cloud.terium.bridge.velocity.listener;

import cloud.terium.bridge.TeriumBridge;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ProxyPingListener {

    @Subscribe
    public void handleProxyPing(ProxyPingEvent event) {
        ServerPing.Builder builder = event.getPing().asBuilder();

        builder.description(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("motd.line1").getAsString() + "\n" +
                TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("motd.line2").getAsString()));
        if (TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("maintenance").getAsBoolean())
            builder.description(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("motd.maintenance.line1").getAsString() + "\n" +
                    TeriumBridge.getInstance().getConfigManager().getCloudBridgeConfig().get("motd.maintenance.line2").getAsString()));

        event.setPing(builder.build());
    }
}