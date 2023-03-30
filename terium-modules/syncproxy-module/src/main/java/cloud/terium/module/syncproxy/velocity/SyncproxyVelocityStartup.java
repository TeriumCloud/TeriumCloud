package cloud.terium.module.syncproxy.velocity;

import cloud.terium.module.syncproxy.manager.ConfigManager;
import cloud.terium.module.syncproxy.listener.CloudListener;
import cloud.terium.module.syncproxy.velocity.listener.ProxyPingListener;
import cloud.terium.module.syncproxy.velocity.listener.ServerConnectedListener;
import cloud.terium.teriumapi.TeriumAPI;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class SyncproxyVelocityStartup {

    private static SyncproxyVelocityStartup instance;
    private final ProxyServer proxyServer;
    private ConfigManager configManager;

    @SneakyThrows
    @Inject
    public SyncproxyVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new ProxyPingListener());
        proxyServer.getEventManager().register(this, new ServerConnectedListener());
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new CloudListener());
    }

    public static SyncproxyVelocityStartup getInstance() {
        return instance;
    }
}