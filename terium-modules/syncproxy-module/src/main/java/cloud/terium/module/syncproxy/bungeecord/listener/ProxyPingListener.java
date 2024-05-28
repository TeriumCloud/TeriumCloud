package cloud.terium.module.syncproxy.bungeecord.listener;

import cloud.terium.module.syncproxy.bungeecord.SyncproxyBungeecordStartup;
import cloud.terium.teriumapi.TeriumAPI;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.BaseComponentSerializer;
import net.md_5.bungee.chat.TextComponentSerializer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.protocol.ProtocolConstants;

public class ProxyPingListener implements Listener {

    @EventHandler
    public void handleProxyPing(ProxyPingEvent event) {
        ServerPing response = event.getResponse();
        ServerPing.Players players = response.getPlayers();

        String motdLine1 = SyncproxyBungeecordStartup.getInstance().getConfigManager().getJson().get("motd.line1").getAsString();
        String motdLine2 = SyncproxyBungeecordStartup.getInstance().getConfigManager().getJson().get("motd.line2").getAsString();
        String motd = motdLine1 + "\n" + motdLine2;

        if (TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().isMaintenance()) {
            String maintenanceLine1 = SyncproxyBungeecordStartup.getInstance().getConfigManager().getJson().get("motd.maintenance.line1").getAsString();
            String maintenanceLine2 = SyncproxyBungeecordStartup.getInstance().getConfigManager().getJson().get("motd.maintenance.line2").getAsString();
            motd = maintenanceLine1 + "\n" + maintenanceLine2;
        }

        final BaseComponent[] bungee;
        if (event.getConnection().getVersion() < ProtocolConstants.MINECRAFT_1_16) {
            bungee = BungeeComponentSerializer.legacy().serialize(MiniMessage.miniMessage().deserialize(motd));
        } else {
            bungee = BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(motd));
        }
        if (BungeeComponentSerializer.isNative()) {
            response.setDescriptionComponent(bungee[0]);
        } else {
            response.setDescription(PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(motd)));
        }
        event.setResponse(response);
    }
}
