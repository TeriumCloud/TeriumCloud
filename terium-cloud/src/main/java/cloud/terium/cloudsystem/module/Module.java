package cloud.terium.cloudsystem.module;

import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.io.File;

@Setter
@AllArgsConstructor
public class Module implements IModule {

    private String name;
    private String author;
    private String version;
    private File file;
    private ModuleType moduleType;

    @Override
    public String getName() {
        return name;
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
    public File getFile() {
        return file;
    }

    @Override
    public ModuleType getModuleType() {
        return moduleType;
    }
}
