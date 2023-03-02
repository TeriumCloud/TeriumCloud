package cloud.terium.plugin.velocity;

import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.velocity.listener.LoginListener;
import cloud.terium.plugin.velocity.listener.ServerConnectedListener;
import com.google.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.kyori.adventure.text.Component;

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
        try {
            teriumBridge = new TeriumPlugin();
            proxyServer.getEventManager().register(this, new LoginListener());
            proxyServer.getEventManager().register(this, new ServerConnectedListener());
            //proxyServer.getCommandManager().register(new CloudCommand().build());

            proxyServer.getConsoleCommandSource().sendMessage(Component.text("§aStartup of velocity terium-plugin successed..."));
        } catch (Exception exception) {
            proxyServer.getConsoleCommandSource().sendMessage(Component.text("§cStartup of velocity terium-plugin failed..."));
            proxyServer.getConsoleCommandSource().sendMessage(Component.text("§7Exception message§f: §c" + exception.getMessage()));
        }
    }

    public static TeriumVelocityStartup getInstance() {
        return instance;
    }
}