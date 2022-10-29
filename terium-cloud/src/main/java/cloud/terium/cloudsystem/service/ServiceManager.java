package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.networking.json.DefaultJsonService;
import cloud.terium.teriumapi.service.*;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ServiceManager implements ICloudServiceProvider, ICloudServiceFactory {

    private final List<ICloudService> minecraftServices;
    private final HashMap<String, CloudService> minecraftServiceCache;

    public ServiceManager() {
        this.minecraftServices = new CopyOnWriteArrayList<>();
        this.minecraftServiceCache = new HashMap<>();
    }

    public void startServiceCheck() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Terium.getTerium().getCloudUtils().isRunning()) {
                    Terium.getTerium().getServiceGroupManager().getAllServiceGroups().forEach(group -> {
                        if (getCloudServicesByGroupName(group.getServiceGroupName()).size() < group.getMaximalServices() && getCloudServicesByGroupName(group.getServiceGroupName()).stream().filter(iCloudService -> iCloudService.getServiceState().equals(CloudServiceState.ONLINE) || iCloudService.getServiceState().equals(CloudServiceState.PREPARING)).toList().size() < group.getMinimalServices()) {
                            new CloudService(group).start();
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    public void addService(CloudService minecraftService) {
        minecraftServices.add(minecraftService);
        minecraftServiceCache.put(minecraftService.getServiceName(), minecraftService);
        new DefaultJsonService(minecraftService);
    }

    public void removeService(CloudService minecraftService) {
        minecraftServices.remove(minecraftService);
        minecraftServiceCache.remove(minecraftService.getServiceName(), minecraftService);
        new DefaultJsonService(minecraftService).delete();
    }

    public void addService(CloudService minecraftService, boolean bridge) {
        minecraftServices.add(minecraftService);
        minecraftServiceCache.put(minecraftService.getServiceName(), minecraftService);
    }

    public void removeService(CloudService minecraftService, boolean bridge) {
        minecraftServices.remove(minecraftService);
        minecraftServiceCache.remove(minecraftService.getServiceName(), minecraftService);
    }

    public int getFreeServiceId(ICloudServiceGroup defaultServiceGroup) {
        AtomicInteger count = new AtomicInteger(1);
        minecraftServices.forEach(service -> {
            if (service.getServiceGroup() == defaultServiceGroup && service.getServiceId() == count.get()) {
                count.getAndIncrement();
            }
        });

        return count.get();
    }

    @Override
    public CloudService getCloudServiceByName(String s) {
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

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup) {
        new CloudService(iCloudServiceGroup.getTemplate(), iCloudServiceGroup, getFreeServiceId(iCloudServiceGroup), iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate) {
        new CloudService(iTemplate, iCloudServiceGroup, getFreeServiceId(iCloudServiceGroup), iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate, int i, int i1) {
        new CloudService(iTemplate, iCloudServiceGroup, getFreeServiceId(iCloudServiceGroup), i, i1);
    }

    @Override
    public void createService(String s, ITemplate iTemplate, CloudServiceType cloudServiceType, int i, int i1) {
        new CloudService(s, iTemplate, null, cloudServiceType, i, ThreadLocalRandom.current().nextInt(20000, 50000), 20, i1);
    }

    @Override
    public void createService(String s, ITemplate iTemplate, CloudServiceType cloudServiceType, int i, int i1, int i2) {
        new CloudService(s, iTemplate, null, cloudServiceType, i, ThreadLocalRandom.current().nextInt(20000, 50000), i1, i2);
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup) {
        new CloudService(s, iCloudServiceGroup.getTemplate(), iCloudServiceGroup, iCloudServiceGroup.getServiceType(), getFreeServiceId(iCloudServiceGroup), iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), iCloudServiceGroup.getMaximumPlayers(), iCloudServiceGroup.getMemory());
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup, int i, int i1) {
        new CloudService(s, iCloudServiceGroup.getTemplate(), iCloudServiceGroup, iCloudServiceGroup.getServiceType(), i, iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), i1, iCloudServiceGroup.getMemory());
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate) {
        new CloudService(s, iTemplate, iCloudServiceGroup, iCloudServiceGroup.getServiceType(), getFreeServiceId(iCloudServiceGroup), iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), iCloudServiceGroup.getMaximumPlayers(), iCloudServiceGroup.getMemory());
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate, int i, int i1) {
        new CloudService(s, iTemplate, iCloudServiceGroup, iCloudServiceGroup.getServiceType(), getFreeServiceId(iCloudServiceGroup), i, i1, iCloudServiceGroup.getMemory());
    }


    @Override
    public void startService(ICloudService iCloudService) {
        minecraftServiceCache.get(iCloudService.getServiceName()).start();
    }
}