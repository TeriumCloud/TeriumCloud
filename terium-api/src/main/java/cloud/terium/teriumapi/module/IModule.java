package cloud.terium.teriumapi.module;

import java.io.File;

public interface IModule {

    String getName();

    String getAuthor();

    String getVersion();

    String getDescription();

    File getFile();

    ModuleType getModuleType();
}
