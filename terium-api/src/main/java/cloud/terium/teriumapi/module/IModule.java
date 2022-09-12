package cloud.terium.teriumapi.module;

import java.io.File;

public interface IModule {

    String getName();

    String getAuthor();

    String getVersion();

    File getFile();

    ModuleType getModuleType();
}
