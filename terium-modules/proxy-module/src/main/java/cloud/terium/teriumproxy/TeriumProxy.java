package cloud.terium.teriumproxy;

import cloud.terium.teriumproxy.listener.LoginListener;
import cloud.terium.teriumproxy.listener.ProxyPingListener;
import cloud.terium.teriumproxy.listener.ServerConnectedListener;
import cloud.terium.teriumproxy.manager.ConfigManager;
import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

@Plugin(
        id = "terium-proxy",
        name = "teriumproxy",
        version = "1.0.0-DEVELOPMENT",
        authors = {"ByRaudy"}
)
@Getter
public class TeriumProxy {

    private static TeriumProxy instance;
    private final ProxyServer proxyServer;
    private final ConfigManager configManager;

    @Inject
    public TeriumProxy(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        EventManager eventManager = proxyServer.getEventManager();
        eventManager.register(this, new LoginListener());
        eventManager.register(this, new ProxyPingListener());
        eventManager.register(this, new ServerConnectedListener());
    }

    public static TeriumProxy getInstance() {
        return instance;
    }
}