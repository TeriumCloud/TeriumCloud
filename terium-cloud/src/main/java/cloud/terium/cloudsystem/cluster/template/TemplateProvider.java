package cloud.terium.cloudsystem.cluster.template;

import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.template.Template;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateProvider;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class TemplateProvider implements ITemplateProvider {

    public final List<ITemplate> templates;

    public TemplateProvider() {
        this.templates = new CopyOnWriteArrayList<>();

        new File("templates//").mkdirs();
        new File("templates//Global//server").mkdirs();
        new File("templates//Global//proxy").mkdirs();
        loadTempaltes();
    }

    private void loadTempaltes() {
        for (File file : new File("templates//").listFiles()) {
            if(!file.getName().equals("Global")) {
                templates.add(new Template(file.getName(), Path.of(file.getPath())));
                Logger.log("Successfully loaded template '§b" + file.getName() + "§f' with path '§b" + file.getPath() + "§f'", LogType.INFO);
            }
        }

        for (File file : new File("templates//Global").listFiles()) {
            if(!file.getName().equals("server") || !file.getName().equals("proxy")) {
                templates.add(new Template("global/" + file.getName(), Path.of(file.getPath())));
                Logger.log("Successfully loaded template '§b" + ("global/" + file.getName()) + "§f' with path '§b" + file.getPath() + "§f'", LogType.INFO);
            }
        }
    }

    @Override
    public Optional<ITemplate> getTemplateByName(String name) {
        for (ITemplate template : templates) {
            if (template.getName().equals(name)) {
                return Optional.ofNullable(template);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ITemplate> getAllTemplates() {
        return templates;
    }
}
