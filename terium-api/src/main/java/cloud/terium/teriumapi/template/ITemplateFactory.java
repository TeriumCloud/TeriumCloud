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
     * Create a template with defined name and node
     *
     * @param name String
     * @param node INode
     */
    ITemplate createTemplate(String name, INode node);

    /**
     * Delete a template by name
     *
     * @param name
     */
    void deleteTemplate(String name);
}