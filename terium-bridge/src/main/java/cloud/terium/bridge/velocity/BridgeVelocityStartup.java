package cloud.terium.bridge.velocity;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.velocity.commands.CloudCommand;
import cloud.terium.bridge.velocity.listener.LoginListener;
import cloud.terium.bridge.velocity.listener.ServerConnectedListener;
import cloud.terium.networking.json.DefaultJsonService;
import cloud.terium.networking.packets.PacketPlayOutSuccessfullServiceShutdown;
import cloud.terium.teriumapi.service.CloudServiceState;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class BridgeVelocityStartup {

    private static BridgeVelocityStartup instance;
    private static TeriumBridge teriumBridge;
    private final ProxyServer proxyServer;

    @Inject
    public BridgeVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        teriumBridge = new TeriumBridge();
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        teriumBridge.initServices(proxyServer);

        proxyServer.getEventManager().register(this, new LoginListener());
        proxyServer.getEventManager().register(this, new ServerConnectedListener());
        proxyServer.getCommandManager().register(new CloudCommand().build());

        proxyServer.getScheduler().buildTask(this, () -> {
            TeriumBridge.getInstance().getCloudPlayerManager().getOnlinePlayers().forEach(iCloudPlayer -> proxyServer.sendMessage(MiniMessage.miniMessage().deserialize("PROXY: " + iCloudPlayer.getUsername() + " / " + iCloudPlayer.getUniqueId() + " / " + iCloudPlayer.getConnectedCloudService().getServiceName())));
        }).repeat(1, TimeUnit.SECONDS).schedule();

        new DefaultJsonService(teriumBridge.getThisName()).setServiceState(CloudServiceState.ONLINE);

        Runnable task = () -> {
            teriumBridge.getThisService().setOnlinePlayers(proxyServer.getPlayerCount());
            teriumBridge.getThisService().setUsedMemory(teriumBridge.usedMemory());
            teriumBridge.getThisService().update();
        };

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        teriumBridge.getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutSuccessfullServiceShutdown(teriumBridge.getThisName()));
    }

    public static BridgeVelocityStartup getInstance() {
        return instance;
    }
}