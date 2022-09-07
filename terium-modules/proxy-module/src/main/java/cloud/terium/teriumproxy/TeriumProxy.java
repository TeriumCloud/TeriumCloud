package cloud.terium.teriumproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
        id = "terium-proxy",
        name = "TeriumProxy",
        version = "1.0.0-SNAPSHOT",
        authors = {"Jxnnik"}
)
@Getter
public class TeriumProxy {

    private static TeriumProxy instance;

    public TeriumProxy() {
        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }

    public static TeriumProxy getInstance() {
        return instance;
    }
}