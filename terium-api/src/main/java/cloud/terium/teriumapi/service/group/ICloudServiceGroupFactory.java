package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public interface ICloudServiceGroupFactory {

    /**
     * Use this methode to create a lobby service group
     *
     * @param name            String
     * @param groupTitle      String
     * @param node            INode
     * @param templates       List<ITemplate>
     * @param version         String
     * @param maximumPlayers  Int
     * @param memory          Int
     * @param minimalServices Int
     * @param maximalServices Int
     */
    default void createLobbyGroup(String name, String groupTitle, INode node, List<ITemplate> templates, String version, boolean isStatic, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultLobbyGroup(name, groupTitle, node, templates, version, false, isStatic, maximumPlayers, memory, minimalServices, maximalServices).initFile();
    }

    /**
     * Use this methode to create a proxy service group
     *
     * @param name            String
     * @param groupTitle      String
     * @param node            INode
     * @param templates       List<ITemplate>
     * @param version         String
     * @param maximumPlayers  Int
     * @param memory          Int
     * @param minimalServices Int
     * @param maximalServices Int
     */
    default void createProxyGroup(String name, String groupTitle, INode node, List<ITemplate> templates, String version, boolean isStatic, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultProxyGroup(name, groupTitle, node, templates, version, false, isStatic, port, maximumPlayers, memory, minimalServices, maximalServices).initFile();
    }

    /**
     * Use this methode to create a server service group
     *
     * @param name            String
     * @param groupTitle      String
     * @param node            INode
     * @param templates       List<ITemplate>
     * @param version         String
     * @param maximumPlayers  Int
     * @param memory          Int
     * @param minimalServices Int
     * @param maximalServices Int
     */
    default void createServerGroup(String name, String groupTitle, INode node, List<ITemplate> templates, String version, boolean isStatic, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultServerGroup(name, groupTitle, node, templates, version, false, isStatic, maximumPlayers, memory, minimalServices, maximalServices).initFile();
    }

    /**
     * Use this methode to delete a service group
     *
     * @param iCloudServiceGroup ICloudServiceGroup
     */
    void deleteServiceGroup(ICloudServiceGroup iCloudServiceGroup);
}