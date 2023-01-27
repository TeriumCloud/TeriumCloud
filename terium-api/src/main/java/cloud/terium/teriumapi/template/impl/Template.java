package cloud.terium.teriumapi.template.impl;

import cloud.terium.teriumapi.template.ITemplate;

import java.nio.file.Path;

public record Template(String name, Path path) implements ITemplate {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Path getPath() {
        return path;
    }
}