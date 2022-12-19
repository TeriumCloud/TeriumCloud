package cloud.terium.cloudsystem.template;

import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateProvider;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateProvider implements ITemplateProvider {

    public final List<ITemplate> templates;

    public TemplateProvider() {
        this.templates = new ArrayList<>();

        new File("templates//").mkdirs();
        loadTempaltes();
    }

    private void loadTempaltes() {
        for (File file : new File("templates//").listFiles()) {
            templates.add(new Template(file.getName(), Path.of(file.getPath())));
            Logger.log("Successfully loaded template '" + file.getName() + "' with path '" + file.getPath() +"'", LogType.INFO);
        }
    }

    @Override
    public ITemplate getTemplateByName(String name) {
        for (ITemplate template : templates) {
            if(template.getName().equals(name)) {
                return template;
            }
        }
        return null;
    }

    @Override
    public List<ITemplate> getAllTemplates() {
        return templates;
    }

    public List<String> getAllTemplatesAsString() {
        List<String> templateNames = new ArrayList<>();
        templates.forEach(template -> {
            templateNames.add(template.getName());
        });

        return templateNames;
    }
}
