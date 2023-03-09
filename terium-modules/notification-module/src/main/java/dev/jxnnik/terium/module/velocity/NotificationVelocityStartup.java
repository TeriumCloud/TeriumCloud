package dev.jxnnik.terium.module.velocity;

import cloud.terium.teriumapi.TeriumAPI;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jxnnik.terium.module.listener.ServiceListener;
import lombok.Getter;

@Getter
public class NotificationVelocityStartup {

    private static NotificationVelocityStartup instance;
    private final ProxyServer proxyServer;

    @Inject
    public NotificationVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
    }

    public static NotificationVelocityStartup getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new ServiceListener());
    }
}