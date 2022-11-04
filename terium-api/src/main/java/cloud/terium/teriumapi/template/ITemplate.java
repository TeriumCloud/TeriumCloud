package cloud.terium.teriumapi.template;

import java.nio.file.Path;

public interface ITemplate {

    /**
     * Get the name of a template
     *
     * @return String
     */
    String getName();

    /**
     * Get the path of a template
     *
     * @return Path
     */
    Path getPath();
}