package cloud.terium.bridge.impl.template;

import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemplateProvider implements ITemplateProvider {

    private final List<ITemplate> templateList;
    private final HashMap<String, ITemplate> templateCache;

    public TemplateProvider() {
        this.templateList = new ArrayList<>();
        this.templateCache = new HashMap<>();
    }

    public void addTemplate(ITemplate template) {
        templateList.add(template);
        templateCache.put(template.name(), template);
    }

    public void removeTemplate(ITemplate template) {
        templateList.remove(template);
        templateCache.remove(template.name());
    }

    @Override
    public ITemplate getTemplateByName(String s) {
        return templateCache.get(s);
    }

    @Override
    public List<ITemplate> getAllTemplates() {
        return templateList;
    }
}
