package cloud.terium.plugin.impl.service.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceGroupProvider implements ICloudServiceGroupProvider {

    private final List<ICloudServiceGroup> cachedCloudServices = new ArrayList<>();

    @Override
    public Optional<ICloudServiceGroup> getServiceGroupByName(String groupName) {
        return getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getGroupName().equals(groupName)).toList().stream().findAny();
    }

    @Override
    public int getOnlineServicesFromServiceGroup(String groupName) {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(groupName).size();
    }

    @Override
    public List<ICloudServiceGroup> getServiceGroupsByGroupTitle(String groupTitle) {
        return getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getGroupTitle().equals(groupTitle)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getServiceGroupsByNode(String nodeName) {
        return getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getGroupNode().getName().equals(nodeName)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getLobbyGroups() {
        return getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getServiceType().equals(ServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getProxyGroups() {
        return getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getServiceType().equals(ServiceType.Proxy)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getServerGroups() {
        return getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getServiceType().equals(ServiceType.Server)).toList();
    }

    @Override
    public List<ICloudServiceGroup> getAllServiceGroups() {
        return cachedCloudServices;
    }
}
