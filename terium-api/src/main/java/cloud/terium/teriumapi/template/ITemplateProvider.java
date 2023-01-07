package cloud.terium.teriumapi.template;

import java.util.List;
import java.util.Optional;

public interface ITemplateProvider {

    /**
     * Get template by name
     *
     * @param name
     * @return ITemplate
     */
    Optional<ITemplate> getTemplateByName(String name);

    /**
     * Returns a list of all exist templates
     *
     * @return List<ITemplate>
     */
    List<ITemplate> getAllTemplates();
}