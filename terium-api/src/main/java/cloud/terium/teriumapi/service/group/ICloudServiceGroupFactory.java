package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import cloud.terium.teriumapi.template.ITemplate;

public interface ICloudServiceGroupFactory {

    /**
     * Use this methode to create a lobby service group
     *
     * @param name
     * @param groupTitle
     * @param node
     * @param version
     * @param maximumPlayers
     * @param memory
     * @param minimalServices
     * @param maximalServices
     */
    default void createLobbyGroup(String name, String groupTitle, String node, ITemplate template, String version, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultLobbyGroup(name, groupTitle, node, template, version, false, maximumPlayers, memory, minimalServices, maximalServices).initFile();
    }

    /**
     * Use this methode to create a proxy service group
     *
     * @param name
     * @param groupTitle
     * @param node
     * @param version
     * @param port
     * @param maximumPlayers
     * @param memory
     * @param minimalServices
     * @param maximalServices
     */
    default void createProxyGroup(String name, String groupTitle, String node, ITemplate template, String version, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultProxyGroup(name, groupTitle, node, template, version, false, port, maximumPlayers, memory, minimalServices, maximalServices).initFile();
    }

    /**
     * Use this methode to create a server service group
     *
     * @param name
     * @param groupTitle
     * @param node
     * @param version
     * @param maximumPlayers
     * @param memory
     * @param minimalServices
     * @param maximalServices
     */
    default void createServerGroup(String name, String groupTitle, String node, ITemplate template, String version, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultServerGroup(name, groupTitle, node, template, version, false, maximumPlayers, memory, minimalServices, maximalServices).initFile();
    }

    /**
     * Use this methode to delete a service group
     *
     * @param iCloudServiceGroup
     */
    void deleteServiceGroup(ICloudServiceGroup iCloudServiceGroup);
}