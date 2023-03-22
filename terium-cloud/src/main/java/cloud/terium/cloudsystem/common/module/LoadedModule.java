package cloud.terium.cloudsystem.common.module;

import cloud.terium.teriumapi.module.ILoadedModule;
import cloud.terium.teriumapi.module.ModuleType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoadedModule implements ILoadedModule {

    private String name;
    private String fileName;
    private String author;
    private String version;
    private String description;
    private String mainClass;
    private boolean reloadable;
    private ModuleType moduleType;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getMainClass() {
        return mainClass;
    }

    @Override
    public boolean isReloadable() {
        return reloadable;
    }

    @Override
    public ModuleType getModuleType() {
        return moduleType;
    }
}
