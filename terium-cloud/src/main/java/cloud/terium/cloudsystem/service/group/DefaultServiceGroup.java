package cloud.terium.cloudsystem.service.group;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.service.ServiceType;

import java.io.File;

public class DefaultServiceGroup implements IServiceGroup {

    private final String name;
    private final String groupTitle;
    private final ServiceType serviceType;
    private final boolean maintenance;
    private final int port;
    private final int maximumPlayers;
    private final int memory;
    private final int minimalServices;
    private final int maximalServices;

    public DefaultServiceGroup(String name, String groupTitle, ServiceType serviceType, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.serviceType = serviceType;
        this.port = -1;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        this.maintenance = false;
        new File("templates//" + name).mkdirs();
    }

    public DefaultServiceGroup(String name, String groupTitle, ServiceType serviceType, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.serviceType = serviceType;
        this.port = port;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        this.maintenance = true;
        new File("templates//" + name).mkdirs();
    }

    public void createGroup() {
        Terium.getTerium().getServiceGroupManager().createServiceGroup(this);
    }

    public boolean hasPort() {
        return port != -1;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String groupTitle() {
        return groupTitle;
    }

    @Override
    public ServiceType serviceType() {
        return serviceType;
    }

    @Override
    public boolean maintenance() {
        return maintenance;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public int maximumPlayers() {
        return maximumPlayers;
    }

    @Override
    public int memory() {
        return memory;
    }

    @Override
    public int minimalServices() {
        return minimalServices;
    }

    @Override
    public int maximalServices() {
        return maximalServices;
    }
}
