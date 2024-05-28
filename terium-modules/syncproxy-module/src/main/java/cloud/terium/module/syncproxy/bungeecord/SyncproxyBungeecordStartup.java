package cloud.terium.module.syncproxy.bungeecord;

import cloud.terium.module.syncproxy.bungeecord.listener.ProxyPingListener;
import cloud.terium.module.syncproxy.bungeecord.listener.ServerConnectedListener;
import cloud.terium.module.syncproxy.listener.CloudListener;
import cloud.terium.module.syncproxy.manager.ConfigManager;
import cloud.terium.teriumapi.TeriumAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyncproxyBungeecordStartup extends Plugin {

    @Getter
    private static SyncproxyBungeecordStartup instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();

        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new ProxyPingListener());
        pluginManager.registerListener(this, new ServerConnectedListener());

        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new CloudListener());
    }
}