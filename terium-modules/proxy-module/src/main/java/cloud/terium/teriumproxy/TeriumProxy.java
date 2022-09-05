package cloud.terium.teriumproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "terium-proxy",
        name = "Terium Proxy",
        version = "1.0.0-RELEASE",
        authors = {"Jxnnik"}
)
public class TeriumProxy {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
