package cloud.terium.module.notification;

import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.module.annotation.Module;
import lombok.Getter;

@Module(name = "notification-module", author = "Jxnnik(ByRaudy)", version = "1.2-OXYGEN", description = "", reloadable = false, moduleType = ModuleType.Proxy)
@Getter
public final class TeriumNotificationModule implements IModule {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}