package cloud.terium.teriumapi.template;

import cloud.terium.teriumapi.cluster.ICluster;

public interface ITemplateFactory {

    /**
     * Create a template with defined name
     *
     * @param name
     */
    void createTemplate(String name);

    /**
     * Create a template with defined name and node
     *
     * @param name
     * @param node
     */
    void createTemplate(String name, ICluster cluster);

    /**
     * Delete a template by name
     *
     * @param name
     */
    void deleteTemplate(String name);
}