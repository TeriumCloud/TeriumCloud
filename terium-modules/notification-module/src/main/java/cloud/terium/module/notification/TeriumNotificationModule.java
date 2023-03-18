package cloud.terium.module.notification;

import cloud.terium.module.notification.manager.ConfigManager;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.module.annotation.Module;
import cloud.terium.teriumapi.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;

@Module(name = "notification-module", author = "Jxnnik(ByRaudy)", version = "1.0.0-OXYGEN", description = "", reloadable = false, moduleType = ModuleType.Proxy)
@Getter
public final class TeriumNotificationModule implements IModule {

    private static TeriumNotificationModule instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();

        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().addHandler(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                if (packet instanceof PacketPlayOutReloadConfig)
                    setConfigManager(new ConfigManager());
            }
        });
    }

    @Override
    public void onDisable() {
    }

    public static TeriumNotificationModule getInstance() {
        return instance;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}