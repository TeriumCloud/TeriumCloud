package cloud.terium.plugin.impl.module;

import cloud.terium.teriumapi.module.ILoadedModule;
import cloud.terium.teriumapi.module.IModuleProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ModuleProvider implements IModuleProvider {

    private final HashMap<String, ILoadedModule> cachedLoadedModules;

    public ModuleProvider() {
        this.cachedLoadedModules = new HashMap<>();
    }

    @Override
    public void loadModule(String path) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
    }

    @Override
    public Optional<ILoadedModule> getModuleByName(String name) {
        return getAllModules().stream().filter(module -> module.getName().equals(name)).findAny();
    }

    @Override
    public List<ILoadedModule> getAllModules() {
        return cachedLoadedModules.values().stream().toList();
    }
}
