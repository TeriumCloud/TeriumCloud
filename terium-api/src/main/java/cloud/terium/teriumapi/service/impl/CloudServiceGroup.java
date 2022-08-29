package cloud.terium.teriumapi.service.impl;

import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public class CloudServiceGroup implements ICloudServiceGroup {

    private String groupName;
    private String groupTitle;
    private String groupNode;
    private CloudServiceType serviceType;
    private boolean maintenance;
    private int port;
    private int memory;
    private int maximumPlayers;
    private int minimumServices;
    private int maximalServices;

    public CloudServiceGroup(String groupName, String groupTitle, String groupNode, CloudServiceType serviceType, boolean maintenanace, int memory, int maximumPlayers, int minimumServices, int maximalServices) {
        this(groupName, groupTitle, groupNode, serviceType, maintenanace, -1, memory, maximumPlayers, minimumServices, maximalServices);
    }

    public CloudServiceGroup(String groupName, String groupTitle, String groupNode, CloudServiceType serviceType, boolean maintenance, int port, int memory, int maximumPlayers, int minimumServices, int maximalServices) {
        this.groupName = groupName;
        this.groupTitle = groupTitle;
        this.groupNode = groupNode;
        this.serviceType = serviceType;
        this.maintenance = maintenance;
        this.port = port;
        this.memory = memory;
        this.maximumPlayers = maximumPlayers;
        this.minimumServices = minimumServices;
        this.maximalServices = maximalServices;
    }

    @Override
    public String getServiceGroupName() {
        return groupName;
    }

    @Override
    public String getGroupTitle() {
        return groupTitle;
    }

    @Override
    public String getServiceGroupNode() {
        return groupNode;
    }

    @Override
    public CloudServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public boolean isMaintenance() {
        return maintenance;
    }

    @Override
    public boolean hasPort() {
        return port != -1;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    @Override
    public int getMemory() {
        return memory;
    }

    @Override
    public int getMinimalServices() {
        return minimumServices;
    }

    @Override
    public int getMaximalServices() {
        return maximalServices;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public void setGroupNode(String groupNode) {
        this.groupNode = groupNode;
    }

    public void setServiceType(CloudServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public void setMinimumServices(int minimumServices) {
        this.minimumServices = minimumServices;
    }

    public void setMaximalServices(int maximalServices) {
        this.maximalServices = maximalServices;
    }
}
