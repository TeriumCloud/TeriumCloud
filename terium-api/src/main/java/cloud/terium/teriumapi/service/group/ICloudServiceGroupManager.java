package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.service.group.impl.*;

import java.util.List;

public interface ICloudServiceGroupManager {

    default void createLobbyGroup(String name, String groupTitle, String node, String version, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultLobbyGroup(name, groupTitle, node, version, false, maximumPlayers, memory, minimalServices, maximalServices);
    }

    default void createProxyGroup(String name, String groupTitle, String node, String version, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultProxyGroup(name, groupTitle, node, version, false, port,  maximumPlayers, memory, minimalServices, maximalServices);
    }

    default void createServerGroup(String name, String groupTitle, String node, String version, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultServerGroup(name, groupTitle, node, version, false, maximumPlayers, memory, minimalServices, maximalServices);
    }

    /**
     * Get a ICloudServiceGroup by group name
     * @param groupName This is the group name
     * @return ICloudServiceGroup This returns the ICloudServiceGroup by the name you typed in param.
     */
    ICloudServiceGroup getServiceGroupByName(String groupName);

    /**
     * Get a ICloudServiceGroup by group title
     * @param groupTitle This is the group title
     * @return List<ICloudServiceGroup> This returns the ICloudServiceGroup by the tite you typed in param.
     */
    List<ICloudServiceGroup> getServiceGroupsByGroupTitle(String groupTitle);

    /**
     * Get a ICloudServiceGroup by node name
     * @param nodeName This is the node name
     * @return List<ICloudServiceGroup> This returns the ICloudServiceGroup by the Node you typed in param.
     */
    List<ICloudServiceGroup> getServiceGroupsByNode(String nodeName);

    /**
     * Get a list of all lobby groups
     * @return List<ICloudServiceGroup> This returns a list of all lobby groups.
     */
    List<ICloudServiceGroup> getLobbyGroups();

    /**
     * Get a list of all proxy groups
     * @return List<ICloudServiceGroup> This returns a list of all proxy groups.
     */
    List<ICloudServiceGroup> getProxyGroups();

    /**
     * Get a list of all server groups
     * @return List<ICloudServiceGroup> This returns a list of all server groups.
     */
    List<ICloudServiceGroup> getServerGroups();

    /**
     * Get a list of all registered groups
     * @return List<ICloudServiceGroup> This returns a list of all groups.
     */
    List<ICloudServiceGroup> getAllServiceGroups();
}
