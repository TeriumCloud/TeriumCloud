package cloud.terium.module.notification.velocity;

import cloud.terium.module.notification.TeriumNotificationModule;
import cloud.terium.module.notification.listener.ServiceListener;
import cloud.terium.module.notification.manager.ConfigManager;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.Packet;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@Getter
public class NotificationVelocityStartup {

    private static NotificationVelocityStartup instance;
    private final ProxyServer proxyServer;
    private ConfigManager configManager;

    @SneakyThrows
    @Inject
    public NotificationVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager();
    }

    public static NotificationVelocityStartup getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().addHandler(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                if (packet instanceof PacketPlayOutReloadConfig)
                    setConfigManager(new ConfigManager());
            }
        });
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new ServiceListener());
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}