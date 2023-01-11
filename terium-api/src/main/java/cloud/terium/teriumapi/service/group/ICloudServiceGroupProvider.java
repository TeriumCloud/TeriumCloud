package cloud.terium.teriumapi.service.group;

import java.util.List;
import java.util.Optional;

/**
 * If you need to 'filter' other things like all methodes here use: getAllServiceGroups().stream().filter();
 */
public interface ICloudServiceGroupProvider {

    /**
     * Get a ICloudServiceGroup by group name
     *
     * @param groupName This is the group name
     * @return Optional<ICloudServiceGroup> This returns the ICloudServiceGroup by the name you typed in param.
     */
    Optional<ICloudServiceGroup> getServiceGroupByName(String groupName);

    /**
     * Get the online services from a ICloudServicegroup by group name
     *
     * @param groupName This is the group name
     * @return Int This returns the online services from a ICloudServiceGroup by the name you typed in param.
     */
    int getOnlineServicesFromServiceGroup(String groupName);

    /**
     * Get a ICloudServiceGroup by group title
     *
     * @param groupTitle This is the group title
     * @return List<ICloudServiceGroup> This returns the ICloudServiceGroup by the tite you typed in param.
     */
    List<ICloudServiceGroup> getServiceGroupsByGroupTitle(String groupTitle);

    /**
     * Get a ICloudServiceGroup by node name
     *
     * @param nodeName This is the node name
     * @return List<ICloudServiceGroup> This returns the ICloudServiceGroup by the Node you typed in param.
     */
    List<ICloudServiceGroup> getServiceGroupsByNode(String nodeName);

    /**
     * Get a list of all lobby groups
     *
     * @return List<ICloudServiceGroup> This returns a list of all lobby groups.
     */
    List<ICloudServiceGroup> getLobbyGroups();

    /**
     * Get a list of all proxy groups
     *
     * @return List<ICloudServiceGroup> This returns a list of all proxy groups.
     */
    List<ICloudServiceGroup> getProxyGroups();

    /**
     * Get a list of all server groups
     *
     * @return List<ICloudServiceGroup> This returns a list of all server groups.
     */
    List<ICloudServiceGroup> getServerGroups();

    /**
     * Get a list of all registered groups
     *
     * @return List<ICloudServiceGroup> This returns a list of all groups.
     */
    List<ICloudServiceGroup> getAllServiceGroups();
}
