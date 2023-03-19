package cloud.terium.plugin.impl.module;

import cloud.terium.teriumapi.module.ILoadedModule;
import cloud.terium.teriumapi.module.IModuleProvider;

import java.util.*;

public class ModuleProvider implements IModuleProvider {

    private final List<ILoadedModule> cachedLoadedModules;

    public ModuleProvider() {
        this.cachedLoadedModules = new LinkedList<>();
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
        return cachedLoadedModules;
    }
}
