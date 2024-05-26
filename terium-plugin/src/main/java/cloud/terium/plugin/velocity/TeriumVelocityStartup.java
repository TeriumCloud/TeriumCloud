package cloud.terium.plugin.velocity;

import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.velocity.command.CloudCommand;
import cloud.terium.plugin.velocity.listener.LoginListener;
import cloud.terium.plugin.velocity.listener.ServerConnectedListener;
import cloud.terium.teriumapi.service.ICloudService;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.net.InetSocketAddress;

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

    public static TeriumVelocityStartup getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        System.out.println("§aTrying to start velocity terium-plugin...");
        try {
            teriumBridge = new TeriumPlugin();
            teriumBridge.getConfigManager().getJson().get("command-aliases").getAsJsonArray().forEach(jsonElement -> proxyServer.getCommandManager().register(new CloudCommand().build(jsonElement.getAsString())));

            proxyServer.getEventManager().register(this, new LoginListener());
            proxyServer.getEventManager().register(this, new ServerConnectedListener());

            System.out.println("§aStartup of velocity terium-plugin succeed...");
        } catch (Exception exception) {
            System.out.println("§cStartup of velocity terium-plugin failed.");
            System.out.println("§7Exception message§f: §c" + exception.getMessage());
        }
    }

    public void executeCommand(String command) {
        TeriumVelocityStartup.getInstance().getProxyServer().getCommandManager().executeAsync(TeriumVelocityStartup.getInstance().getProxyServer().getConsoleCommandSource(), command);
    }

    public void disconnectPlayer(Player player, String message) {
        player.disconnect(message.contains("§") ? Component.text(message) : MiniMessage.miniMessage().deserialize(message));
    }

    public void registerServer(ICloudService cloudService, InetSocketAddress address) {
        proxyServer.registerServer(new ServerInfo(cloudService.getServiceName(), address));
    }


    public boolean checkServerIsRegistered(String serviceName) {
        return TeriumVelocityStartup.getInstance().getProxyServer().getServer(serviceName).isPresent();
    }
}