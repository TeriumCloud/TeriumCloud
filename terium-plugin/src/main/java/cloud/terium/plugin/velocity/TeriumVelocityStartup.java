package cloud.terium.plugin.velocity;

import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.velocity.listener.LoginListener;
import cloud.terium.plugin.velocity.listener.ServerConnectedListener;
import com.google.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

@Getter
public class TeriumVelocityStartup {

    private static TeriumVelocityStartup instance;
    private static TeriumPlugin teriumBridge;
    private final ProxyServer proxyServer;

    @Inject
    public TeriumVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        System.out.println("§aTrying to start velocity terium-plugin...");
        try {
            teriumBridge = new TeriumPlugin();

            proxyServer.getEventManager().register(this, new LoginListener());
            proxyServer.getEventManager().register(this, new ServerConnectedListener());
            //proxyServer.getCommandManager().register(new CloudCommand().build());\

            System.out.println("§aStartup of velocity terium-plugin successed.");
        } catch (Exception exception) {
            System.out.println("§cStartup of velocity terium-plugin failed.");
            System.out.println("§7Exception message§f: §c" + exception.getMessage());
        }
    }

    public static TeriumVelocityStartup getInstance() {
        return instance;
    }
}