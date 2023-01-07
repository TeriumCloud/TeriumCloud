package cloud.terium.teriumapi.module;

import java.util.List;
import java.util.Optional;

public interface IModuleProvider {

    /**
     * load a module by path
     *
     * @param path
     */
    void loadModule(String path);

    /**
     * Get a module by name
     *
     * @param name
     * @return ILoadedModule This returns the module by name.
     */
    Optional<ILoadedModule> getModuleByName(String name);

    /**
     * Get all loaded modules
     *
     * @return List<ILoadedModule> This returns a list of all loaded modules.
     */
    List<ILoadedModule> getAllModules();
}
