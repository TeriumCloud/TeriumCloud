package cloud.terium.teriumapi.module;

import java.util.List;

public interface IModuleManager {

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
     * @return IModule This returns the module by name.
     */
    IModule getModuleByName(String name);

    /**
     * Get all loaded modules
     *
     * @return List<IModule> This returns a list of all loaded modules.
     */
    List<IModule> getAllModules();
}
