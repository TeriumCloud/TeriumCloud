package cloud.terium.extension.impl.template;

import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemplateProvider implements ITemplateProvider {

    private final List<ITemplate> cachedTemplates;

    public TemplateProvider() {
        this.cachedTemplates = new ArrayList<>();
    }

    @Override
    public Optional<ITemplate> getTemplateByName(String name) {
        return cachedTemplates.stream().filter(template -> template.getName().equals(name)).findAny();
    }

    @Override
    public List<ITemplate> getAllTemplates() {
        return cachedTemplates;
    }
}
