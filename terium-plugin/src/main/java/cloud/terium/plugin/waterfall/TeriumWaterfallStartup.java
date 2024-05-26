package cloud.terium.plugin.waterfall;

import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.waterfall.command.CloudCommand;
import cloud.terium.plugin.waterfall.listener.LoginListener;
import cloud.terium.plugin.waterfall.listener.ServerConnectedListener;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetSocketAddress;

public class TeriumWaterfallStartup extends Plugin {

    @Getter
    private static TeriumWaterfallStartup instance;
    private static TeriumPlugin teriumBridge;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("§aTrying to start waterfall terium-plugin...");

        try {
            teriumBridge = new TeriumPlugin();
            teriumBridge.getConfigManager().getJson().get("command-aliases").getAsJsonArray().forEach(jsonElement ->
                    getProxy().getPluginManager().registerCommand(this, new CloudCommand(jsonElement.getAsString()))
            );

            getProxy().getPluginManager().registerListener(this, new LoginListener());
            getProxy().getPluginManager().registerListener(this, new ServerConnectedListener());

            System.out.println("§aStartup of waterfall terium-plugin succeed...");
        } catch (Exception exception) {
            System.out.println("§cStartup of waterfall terium-plugin failed.");
            System.out.println("§7Exception message§f: §c" + exception.getMessage());
        }
    }

    public void executeCommand(String command) {
        ProxyServer.getInstance().getPluginManager().dispatchCommand(
                ProxyServer.getInstance().getConsole(), command
        );
    }

    public void disconnectPlayer(ProxiedPlayer player, String message) {
        player.disconnect(new TextComponent(message));
    }

    public void registerServer(ICloudService cloudService, InetSocketAddress address) {
        ProxyServer.getInstance().getServers().put(cloudService.getServiceName(), getProxy().constructServerInfo(cloudService.getServiceName(), address, "A default terium-cloud service.", false));
    }

    public boolean checkServerIsRegistered(String serviceName) {
        return ProxyServer.getInstance().getServers().containsKey(serviceName);
    }
}
