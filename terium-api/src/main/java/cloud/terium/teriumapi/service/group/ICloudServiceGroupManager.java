package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.service.group.impl.*;

import java.util.List;

public interface ICloudServiceGroupManager {

    default void createLobbyGroup(String name, String groupTitle, String node, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultLobbyGroup(name, groupTitle, node, false, maximumPlayers, memory, minimalServices, maximalServices);
    }

    default void createProxyGroup(String name, String groupTitle, String node, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultProxyGroup(name, groupTitle, node, false, port,  maximumPlayers, memory, minimalServices, maximalServices);
    }

    default void createServerGroup(String name, String groupTitle, String node, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new DefaultServerGroup(name, groupTitle, node, false, maximumPlayers, memory, minimalServices, maximalServices);
    }

    ICloudServiceGroup getServiceGroupByName(String groupName);

    List<ICloudServiceGroup> getServiceGroupsByGroupTitle(String groupTitle);

    List<ICloudServiceGroup> getServiceGroupsByWrapper(String wrapperName);

    List<ICloudServiceGroup> getLobbyGroups();

    List<ICloudServiceGroup> getProxyGroups();

    List<ICloudServiceGroup> getServerGroups();

    List<ICloudServiceGroup> getAllServiceGroups();
}
