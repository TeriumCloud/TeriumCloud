package cloud.terium.plugin.impl.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ServiceProvider implements ICloudServiceProvider {

    private final List<ICloudService> cachedServices;

    public ServiceProvider() {
        this.cachedServices = new LinkedList<>();
    }

    @Override
    public Optional<ICloudService> getServiceByName(String serviceName) {
        return cachedServices.stream().filter(cloudService -> cloudService.getServiceName().equals(serviceName)).findAny();
    }

    @Override
    public List<ICloudService> getServicesByGroupName(String serviceGroup) {
        return cachedServices.stream().filter(cloudService -> cloudService.getServiceGroup().getGroupName().equals(serviceGroup)).toList();
    }

    @Override
    public List<ICloudService> getServicesByGroupTitle(String groupTitle) {
        return cachedServices.stream().filter(cloudService -> cloudService.getServiceGroup().getGroupTitle().equals(groupTitle)).toList();
    }

    @Override
    public List<ICloudService> getAllLobbyServices() {
        return cachedServices.stream().filter(cloudService -> cloudService.getServiceType().equals(ServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudService> getAllServices() {
        return cachedServices;
    }

    @Override
    public int getFreeServiceId(ICloudServiceGroup iCloudServiceGroup) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
        return 0;
    }

    @Override
    public long gloablUsedMemory() {
        return TeriumAPI.getTeriumAPI().getProvider().getThisService().getUsedMemory();
    }

    @Override
    public void putServiceId(ICloudServiceGroup iCloudServiceGroup, int i) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
    }

    @Override
    public void removeServiceId(ICloudServiceGroup iCloudServiceGroup, int i) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
    }

    @Override
    public void addService(ICloudService iCloudService) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
    }

    @Override
    public void removeService(ICloudService iCloudService) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
    }
}
