package cloud.terium.plugin.velocity;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.plugin.velocity.command.CloudCommand;
import cloud.terium.plugin.velocity.listener.LoginListener;
import cloud.terium.plugin.velocity.listener.ServerConnectedListener;
import cloud.terium.plugin.velocity.listener.cloud.VelocityMinestomHandler;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.awt.desktop.AppForegroundListener;
import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class TeriumVelocityStartup extends TeriumExtension {

    @Getter
    private static TeriumVelocityStartup instance;
    private final ProxyServer proxyServer;

    @Inject
    public TeriumVelocityStartup(ProxyServer proxyServer) {
        super();
        instance = this;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        System.out.println("§aTrying to start velocity terium-plugin...");
        try {
            getProxyServer().getScheduler().buildTask(this, () -> {
                this.successfulStart();
                this.getTeriumNetworking().addHandler(new VelocityMinestomHandler());
            }).delay((long) 1.5, TimeUnit.SECONDS).schedule();
            getConfigManager().getJson().get("command-aliases").getAsJsonArray().forEach(jsonElement -> proxyServer.getCommandManager().register(new CloudCommand().build(jsonElement.getAsString())));

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

    @Override
    public void connectPlayer(UUID player, ICloudService cloudService) {
        getProxyServer().getPlayer(player).ifPresent(parsedPlayer ->
                parsedPlayer.createConnectionRequest(TeriumVelocityStartup.getInstance().getProxyServer().getServer(cloudService.getServiceName()).orElse(null)).connect());
    }

    @Override
    public void disconnectPlayer(UUID player, String message) {
        getProxyServer().getPlayer(player).ifPresent(parsedPlayer ->
                parsedPlayer.disconnect(message.contains("§") ? Component.text(message) : MiniMessage.miniMessage().deserialize(message)));
    }

    public void registerServer(ICloudService cloudService, InetSocketAddress address) {
        proxyServer.registerServer(new ServerInfo(cloudService.getServiceName(), address));
    }

    @Override
    public void unregisterServer(ICloudService cloudService) {
        proxyServer.unregisterServer(proxyServer.getServer(cloudService.getServiceName()).orElseThrow().getServerInfo());
    }

    @Override
    public @NotNull Optional<ICloudService> getFallback(final UUID player) {
        Player parsedPlayer = getProxyServer().getPlayer(player).orElseThrow();

        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.getServiceGroup().getServiceType().equals(ServiceType.Proxy))
                .filter(service -> service.getServiceGroup().getServiceType().equals(ServiceType.Lobby))
                .filter(service -> !service.isLocked())
                .filter(service -> (parsedPlayer.getCurrentServer().isEmpty()
                        || !parsedPlayer.getCurrentServer().get().getServerInfo().getName().equals(service.getServiceName())))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers));
    }

    public boolean checkServerIsRegistered(String serviceName) {
        return TeriumVelocityStartup.getInstance().getProxyServer().getServer(serviceName).isPresent();
    }
}