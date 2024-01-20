package cloud.terium.module.permission.velocity;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.base.TeriumPermissionBaseVelocity;
import cloud.terium.module.permission.velocity.command.CloudPermissionsCommand;
import cloud.terium.module.permission.velocity.listener.LoginListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@Getter
public class PermissionVelocityStartup {

    private static PermissionVelocityStartup instance;
    private final ProxyServer proxyServer;

    @SneakyThrows
    @Inject
    public PermissionVelocityStartup(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
    }

    public static PermissionVelocityStartup getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new LoginListener());
        proxyServer.getEventManager().register(this, new PermissionSetup());
        proxyServer.getCommandManager().register(new CloudPermissionsCommand().build("cperms"));

        proxyServer.getScheduler().buildTask(this, () -> new TeriumPermissionModule().onEnable()).delay(1, TimeUnit.SECONDS).schedule();
    }
}

class PermissionSetup {

    private final PermissionProvider permissionProvider = new TeriumPermissionBaseVelocity();

    @Subscribe
    public void handlePermissionSetup(PermissionsSetupEvent event) {
        event.setProvider(permissionProvider);
    }
}