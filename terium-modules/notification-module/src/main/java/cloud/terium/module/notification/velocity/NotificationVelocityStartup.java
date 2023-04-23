package cloud.terium.module.notification.velocity;

import cloud.terium.module.notification.listener.PlayerConnectionListener;
import cloud.terium.module.notification.listener.ServiceListener;
import cloud.terium.module.notification.manager.ConfigManager;
import cloud.terium.teriumapi.TeriumAPI;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class NotificationVelocityStartup {

    private static NotificationVelocityStartup instance;
    private final ProxyServer proxyServer;
    private ConfigManager configManager;

    @SneakyThrows
    @Inject
    public NotificationVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager();
    }

    public static NotificationVelocityStartup getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new ServiceListener());
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new PlayerConnectionListener());
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}