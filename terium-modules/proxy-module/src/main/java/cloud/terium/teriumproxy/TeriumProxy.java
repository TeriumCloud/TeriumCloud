package cloud.terium.teriumproxy;

import cloud.terium.teriumproxy.manager.ConfigManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import lombok.Getter;

@Plugin(
        id = "terium-proxy",
        name = "teriumproxy",
        version = "1.0.0-DEVELOPMENT",
        authors = {"ByRaudy"}
)
@Getter
public class TeriumProxy {

    private static TeriumProxy instance;
    private final ConfigManager configManager;

    public TeriumProxy() {
        instance = this;
        configManager = new ConfigManager();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }

    public static TeriumProxy getInstance() {
        return instance;
    }
}