package cloud.terium.teriumapi.module;

import java.io.File;
import java.io.Serializable;

public interface ILoadedModule extends Serializable {

    String getName();

    String getFileName();

    String getAuthor();

    String getVersion();

    String getDescription();

    String getMainClass();

    ModuleType getModuleType();
}
