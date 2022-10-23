package cloud.terium.bridge.impl.template;

import cloud.terium.teriumapi.template.ITemplate;

import java.nio.file.Path;

public class Template implements ITemplate {

    private String name;
    private Path path;

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
