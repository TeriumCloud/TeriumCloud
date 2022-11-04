package cloud.terium.teriumapi.template;

import java.util.List;

public interface ITemplateProvider {

    /**
     * Get template by name
     *
     * @param name
     * @return ITemplate
     */
    ITemplate getTemplateByName(String name);

    /**
     * Returns a list of all exist templates
     *
     * @return List<ITemplate>
     */
    List<ITemplate> getAllTemplates();
}