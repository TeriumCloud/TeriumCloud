package cloud.terium.module.notification.bungeecord;

import cloud.terium.module.notification.listener.PlayerConnectionListener;
import cloud.terium.module.notification.listener.ServiceListener;
import cloud.terium.module.notification.manager.ConfigManager;
import cloud.terium.teriumapi.TeriumAPI;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class NotificationBungeeCordStartup extends Plugin {

    private static NotificationBungeeCordStartup instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();

        // Register Terium API listeners if required
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new ServiceListener());
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new PlayerConnectionListener());
    }

    public static NotificationBungeeCordStartup getInstance() {
        return instance;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
