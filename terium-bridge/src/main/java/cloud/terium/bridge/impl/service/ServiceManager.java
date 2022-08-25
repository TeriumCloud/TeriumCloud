package cloud.terium.bridge.impl.service;

import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceManager;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ServiceManager implements ICloudServiceManager {

    private final List<ICloudService> minecraftServices;
    private final HashMap<String, ICloudService> minecraftServiceCache;

    public ServiceManager() {
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

    public List<ICloudService> getAllLobbyServices() {
        return minecraftServices.stream().filter(iCloudService -> iCloudService.getServiceType().equals(CloudServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudService> getAllCloudServices() {
        return minecraftServices;
    }
}