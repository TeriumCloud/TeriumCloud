package cloud.terium.teriumapi.template;

import java.io.Serializable;
import java.nio.file.Path;

public interface ITemplate extends Serializable {

    /**
     * Get the name of the template
     *
     * @return String
     */
    String getName();

    /**
     * Get the path of the template
     *
     * @return Path
     */
    Path getPath();
}