package cloud.terium.teriumapi.template;

import cloud.terium.teriumapi.node.INode;

public interface ITemplateFactory {

    /**
     * Create a template with defined name
     *
     * @param name String
     */
    ITemplate createTemplate(String name);

    /**
     * Delete a template by name
     *
     * @param name
     */
    void deleteTemplate(String name);
}