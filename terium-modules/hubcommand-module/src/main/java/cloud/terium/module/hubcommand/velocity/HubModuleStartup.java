package cloud.terium.module.hubcommand.velocity;

import cloud.terium.module.command.HubCommand;
import cloud.terium.module.manager.ConfigManager;
import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

@Getter
public class HubModuleStartup {

    private static HubModuleStartup instance;
    private ProxyServer proxyServer;
    private ConfigManager configManager;

    @Inject
    public HubModuleStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager();

        this.proxyServer.getCommandManager().register(this.proxyServer.getCommandManager().metaBuilder("hub").build(), new HubCommand());
    }

    public static HubModuleStartup getInstance() {
        return instance;
    }
}
