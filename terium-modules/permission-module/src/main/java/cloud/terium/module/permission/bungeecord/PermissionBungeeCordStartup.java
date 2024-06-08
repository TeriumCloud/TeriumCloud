package cloud.terium.module.permission.bungeecord;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.base.TeriumPermissionBaseBungeeCord;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class PermissionBungeeCordStartup extends Plugin {

    @Getter
    private static PermissionBungeeCordStartup instance;

    @Override
    public void onEnable() {
        instance = this;
        getProxy().getPluginManager().registerListener(this, new TeriumPermissionBaseBungeeCord());
        getProxy().getScheduler().schedule(this, () -> new TeriumPermissionModule().onEnable(), 2, TimeUnit.SECONDS);
    }
}
