package cloud.terium.cloudsystem.template;

import cloud.terium.teriumapi.template.ITemplate;

import java.nio.file.Path;

public class Template implements ITemplate {

    private final String name;
    private final Path path;

    public Template(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Path getPath() {
        return path;
    }
}
