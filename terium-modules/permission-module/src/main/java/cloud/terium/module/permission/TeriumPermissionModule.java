package cloud.terium.module.permission;

import cloud.terium.module.permission.permission.group.PermissionGroupManager;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.module.annotation.Module;
import lombok.Getter;

@Module(name = "permission", author = "Jxnnik(ByRaudy)", version = "1.2-OXYGEN", description = "", reloadable = true, moduleType = ModuleType.Proxy)
@Getter
public class TeriumPermissionModule implements IModule {

    private static TeriumPermissionModule instance;
    private PermissionGroupManager permissionGroupManager;

    @Override
    public void onEnable() {
        instance = this;
        permissionGroupManager = new PermissionGroupManager();
        new TeriumPermissionModule();
    }

    @Override
    public void onDisable() {
    }

    public static TeriumPermissionModule getInstance() {
        return instance;
    }
}
