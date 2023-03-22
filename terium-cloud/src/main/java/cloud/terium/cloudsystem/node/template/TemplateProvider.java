package cloud.terium.cloudsystem.node.template;

import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateProvider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class TemplateProvider implements ITemplateProvider {

    public final List<ITemplate> templates;

    public TemplateProvider() {
        this.templates = new CopyOnWriteArrayList<>();
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
