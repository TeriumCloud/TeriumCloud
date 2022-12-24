package cloud.terium.teriumapi.module;

import java.io.File;
import java.io.Serializable;

public interface IModule extends Serializable {

    String getName();

    String getAuthor();

    String getVersion();

    String getDescription();

    File getFile();

    ModuleType getModuleType();
}
