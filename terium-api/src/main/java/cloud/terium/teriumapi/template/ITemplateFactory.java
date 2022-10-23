package cloud.terium.teriumapi.template;

import cloud.terium.teriumapi.node.INode;

public interface ITemplateFactory {

    /**
     * Create a template with defined name
     * @param name
     */
    void createTemplate(String name);

    /**
     * Create a template with defined name and node
     * @param name
     * @param node
     */
    void createTemplate(String name, INode node);

    /**
     * Delete a template by name
     * @param name
     */
    void deleteTemplate(String name);
}