package cloud.terium.plugin.bungeecord;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.plugin.bungeecord.command.CloudCommand;
import cloud.terium.plugin.bungeecord.listener.LoginListener;
import cloud.terium.plugin.bungeecord.listener.ServerConnectedListener;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class TeriumBungeecordStartup extends Plugin {

    @Getter
    private static TeriumBungeecordStartup instance;
    private static TeriumExtension teriumBridge;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("§aTrying to start waterfall terium-plugin...");

        try {
            teriumBridge = new TeriumExtension() {
                @Override
                public Optional<ICloudService> getFallback(UUID player) {
                    return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream()
                            .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                            .filter(service -> !service.getServiceGroup().getServiceType().equals(ServiceType.Proxy))
                            .filter(service -> service.getServiceGroup().getServiceType().equals(ServiceType.Lobby))
                            .filter(service -> !service.isLocked())
                            /*.filter(service -> (player.getServer() != null)
                                    && !player.getServer().getInfo().getName().equals(service.getServiceName()))*/
                            .min(Comparator.comparing(ICloudService::getOnlinePlayers));
                }

                @Override
                public void executeCommand(String command) {
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(
                            ProxyServer.getInstance().getConsole(), command
                    );
                }

                @Override
                public void connectPlayer(UUID player, ICloudService cloudService) {
                    if(getProxy().getServerInfo(cloudService.getServiceName()) == null && getProxy().getPlayer(player) == null)
                        return;

                    getProxy().getPlayer(player).connect(getProxy().getServerInfo(cloudService.getServiceName()));
                }

                @Override
                public void disconnectPlayer(UUID player, String message) {
                    if (getProxy().getPlayer(player) == null)
                        return;

                    getProxy().getPlayer(player).disconnect(new TextComponent(message));
                }

                @Override
                public void registerServer(ICloudService cloudService, InetSocketAddress address) {
                    getProxy().getServers().put(cloudService.getServiceName(), getProxy().constructServerInfo(cloudService.getServiceName(), address, "A default terium-cloud service.", false));
                }

                @Override
                public void unregisterServer(ICloudService cloudService) {
                    if (getProxy().getServers().get(cloudService.getServiceName()) == null) {
                        System.out.println("This server isn't registered! (" + cloudService.getServiceName() + ")");
                        return;
                    }

                    getProxy().getServers().remove(cloudService.getServiceName());
                }

                @Override
                public boolean checkServerIsRegistered(String serviceName) {
                    return getProxy().getServers().containsKey(serviceName);
                }
            };
            teriumBridge.getConfigManager().getJson().get("command-aliases").getAsJsonArray().forEach(jsonElement ->
                    getProxy().getPluginManager().registerCommand(this, new CloudCommand(jsonElement.getAsString())));

            getProxy().getPluginManager().registerListener(this, new LoginListener());
            getProxy().getPluginManager().registerListener(this, new ServerConnectedListener());

            System.out.println("§aStartup of waterfall terium-plugin succeed...");
        } catch (Exception exception) {
            System.out.println("§cStartup of waterfall terium-plugin failed.");
            System.out.println("§7Exception message§f: §c" + exception.getMessage());
        }
    }
}
