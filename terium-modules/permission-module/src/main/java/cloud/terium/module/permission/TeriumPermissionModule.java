package cloud.terium.module.permission;

import cloud.terium.module.permission.cloud.CloudListener;
import cloud.terium.module.permission.cloud.PermissionPipeHandler;
import cloud.terium.module.permission.permission.group.GroupFileManager;
import cloud.terium.module.permission.permission.group.PermissionGroupManager;
import cloud.terium.module.permission.permission.user.PermissionUserManager;
import cloud.terium.module.permission.permission.user.UserFileManager;
import cloud.terium.module.permission.utils.ApplicationType;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.module.annotation.Module;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Arrays;

@Module(name = "permission", author = "Jxnnik(ByRaudy)", version = "1.3-OXYGEN", description = "", reloadable = true, moduleType = ModuleType.ALL)
@Getter
@Setter
public class TeriumPermissionModule implements IModule {

    private static TeriumPermissionModule instance;
    private PermissionGroupManager permissionGroupManager;
    private PermissionUserManager permissionUserManager;
    private UserFileManager userFileManager;

    @Override
    public void onEnable() {
        instance = this;
        this.permissionGroupManager = new PermissionGroupManager();
        this.permissionUserManager = new PermissionUserManager();

        Arrays.stream(new File("modules/permission/groups").listFiles()).toList().forEach(file -> new GroupFileManager(file.getName().replace(".json", ""), ApplicationType.MODULE).loadFile());

        this.userFileManager = new UserFileManager(TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN);
        this.userFileManager.loadFile();
        if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null) {
            TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new CloudListener());
        }
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().addHandler(new PermissionPipeHandler(TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN));
    }

    @Override
    public void onDisable() {
    }

    public static TeriumPermissionModule getInstance() {
        return instance;
    }
}
