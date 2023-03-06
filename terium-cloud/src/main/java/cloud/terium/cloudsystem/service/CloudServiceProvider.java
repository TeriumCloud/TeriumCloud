package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CloudServiceProvider implements ICloudServiceProvider {

    private final HashMap<String, ICloudService> cloudServiceCache;
    private final HashMap<ICloudServiceGroup, List<Integer>> cloudServiceIdCache;

    public CloudServiceProvider() {
        this.cloudServiceCache = new HashMap<>();
        this.cloudServiceIdCache = new HashMap<>();
        TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().forEach(cloudServiceGroup -> cloudServiceIdCache.put(cloudServiceGroup, new LinkedList<>()));
    }

    public void startServiceCheck() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (TeriumCloud.getTerium().getCloudUtils().isRunning() && gloablUsedMemory() < TeriumCloud.getTerium().getCloudConfig().memory()) {
                    TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
                        if (getCloudServicesByGroupName(group.getGroupName()).size() < group.getMaxServices() &&
                                getCloudServicesByGroupName(group.getGroupName()).stream().filter(iCloudService -> iCloudService.getServiceState().equals(ServiceState.ONLINE) ||
                                        iCloudService.getServiceState().equals(ServiceState.PREPARING)).toList().size() < group.getMinServices()) {
                            TeriumCloud.getTerium().getServiceFactory().createService(group);
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    public void startServiceStopCheck() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (TeriumCloud.getTerium().getCloudUtils().isRunning()) {
                    TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
                        if (getCloudServicesByGroupName(group.getGroupName()).size() > group.getMinServices()) {
                            getCloudServicesByGroupName(group.getGroupName()).stream().filter(iCloudService -> iCloudService.getServiceState().equals(ServiceState.ONLINE) && iCloudService.getOnlinePlayers() == 0).sorted(Comparator.comparing(ICloudService::getServiceId).reversed()).findFirst().ifPresent(ICloudService::shutdown);
                        }
                    });
                }
            }
        }, 0, 60000);
    }

    public int getFreeServiceId(ICloudServiceGroup cloudServiceGroup) {
        AtomicInteger integer = new AtomicInteger(1);
        for (int i = 0; i < getCloudServicesByGroupName(cloudServiceGroup.getGroupName()).size(); i++) {
            if (cloudServiceIdCache.get(cloudServiceGroup).contains(integer.get())) {
                integer.getAndIncrement();
            }
        }
        return integer.get();
    }

    public void putServiceId(ICloudServiceGroup serviceGroup, int id) {
        cloudServiceIdCache.get(serviceGroup).add(id);
    }

    public void removeServiceId(ICloudServiceGroup serviceGroup, int id) {
        cloudServiceIdCache.get(serviceGroup).remove((Object)id);
    }

    public long gloablUsedMemory() {
        AtomicLong globalUsedMemory = new AtomicLong();
        TeriumCloud.getTerium().getServiceProvider().getAllCloudServices().forEach(cloudService -> globalUsedMemory.getAndAdd(cloudService.getMaxMemory()));

        return globalUsedMemory.get();
    }

    public void addService(ICloudService cloudService) {
        cloudServiceCache.put(cloudService.getServiceName(), cloudService);
    }

    public void removeService(ICloudService cloudService) {
        cloudServiceCache.remove(cloudService.getServiceName(), cloudService);
    }

    @Override
    public Optional<ICloudService> getCloudServiceByName(String serviceName) {
        return Optional.ofNullable(cloudServiceCache.get(serviceName));
    }

    @Override
    public List<ICloudService> getCloudServicesByGroupName(String serviceGroup) {
        return cloudServiceCache.values().stream().filter(cloudService -> cloudService.getServiceGroup().getGroupName().equals(serviceGroup)).toList();
    }

    @Override
    public List<ICloudService> getCloudServicesByGroupTitle(String groupTitle) {
        return cloudServiceCache.values().stream().filter(cloudService -> cloudService.getServiceGroup().getGroupTitle().equals(groupTitle)).toList();
    }

    @Override
    public List<ICloudService> getAllLobbyServices() {
        return cloudServiceCache.values().stream().filter(cloudService -> cloudService.getServiceGroup().getServiceType().equals(ServiceType.Lobby)).toList();
    }

    @Override
    public List<ICloudService> getAllCloudServices() {
        return cloudServiceCache.values().stream().toList();
    }
}
