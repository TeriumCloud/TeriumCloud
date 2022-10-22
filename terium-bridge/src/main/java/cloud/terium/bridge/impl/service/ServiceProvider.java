package cloud.terium.bridge.impl.service;

import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ServiceProvider implements ICloudServiceProvider {

    private final List<ICloudService> minecraftServices;
    private final HashMap<String, ICloudService> minecraftServiceCache;

    public ServiceProvider() {
        this.minecraftServices = new CopyOnWriteArrayList<>();
        this.minecraftServiceCache = new HashMap<>();
    }

    public void addService(ICloudService iCloudService) {
        minecraftServices.add(iCloudService);
        minecraftServiceCache.put(iCloudService.getServiceName(), iCloudService);
    }

    public void removeService(ICloudService iCloudService) {
        minecraftServices.remove(iCloudService);
        minecraftServiceCache.remove(iCloudService.getServiceName(), iCloudService);
    }

    @Override
    public ICloudService getCloudServiceByName(String s) {
        return minecraftServiceCache.get(s);
    }

    @Override
    public List<ICloudService> getCloudServicesByGroupName(String s) {
        return minecraftServices.stream().filter(iCloudService -> iCloudService.getServiceGroup().getServiceGroupName().equals(s)).toList();
    }

    @Override
    public List<ICloudService> getCloudServicesByGroupTitle(String s) {
        return minecraftServices.stream().filter(iCloudService -> iCloudService.getServiceGroup().getGroupTitle().equals(s)).toList();
    }

    @Override
    public List<ICloudService> getAllLobbyServices() {
        return minecraftServices.stream().filter(iCloudService -> iCloudService.getServiceType().equals(CloudServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudService> getAllCloudServices() {
        return minecraftServices;
    }
}