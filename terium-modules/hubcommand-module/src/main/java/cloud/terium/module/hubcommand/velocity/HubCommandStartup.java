package cloud.terium.module.hubcommand.velocity;

import cloud.terium.module.hubcommand.command.HubCommand;
import cloud.terium.module.hubcommand.manager.ConfigManager;
import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

@Getter
public class HubCommandStartup {

    private static HubCommandStartup instance;
    private final ProxyServer proxyServer;
    private final ConfigManager configManager;

    @Inject
    public HubCommandStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager();

        this.proxyServer.getCommandManager().register(this.proxyServer.getCommandManager().metaBuilder("hub").build(), new HubCommand());
    }

    public static HubCommandStartup getInstance() {
        return instance;
    }
}
