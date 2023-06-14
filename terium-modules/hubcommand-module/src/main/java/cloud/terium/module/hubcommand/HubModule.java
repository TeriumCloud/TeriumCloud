package cloud.terium.module.hubcommand;

import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import lombok.Getter;

@Getter
@Module(name = "hubmodule", author = "veteex", version = "1.0", description = "", reloadable = false, moduleType = ModuleType.Proxy)
public class HubModule implements IModule {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
