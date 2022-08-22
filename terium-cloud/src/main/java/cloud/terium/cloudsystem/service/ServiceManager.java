package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.networking.json.DefaultJsonService;
import cloud.terium.cloudsystem.service.group.DefaultServiceGroup;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ServiceManager {

    private final List<MinecraftService> minecraftServices;
    private final HashMap<String, MinecraftService> minecraftServiceCache;

    public ServiceManager() {
        this.minecraftServices = new CopyOnWriteArrayList<>();
        this.minecraftServiceCache = new HashMap<>();
    }

    public void startServiceCheck() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Terium.getTerium().getCloudUtils().isRunning()) {
                    Terium.getTerium().getServiceGroupManager().getServiceGroups().forEach(group -> {
                        if (getServicesByGroupName(group.name()).size() <= group.maximalServices() && getServicesByGroupName(group.name()).size() < group.minimalServices()) {
                            new MinecraftService(group).start();
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    public void startCloudServices() {
        Terium.getTerium().getServiceGroupManager().getServiceGroups().forEach(group -> {
            System.out.println(getServicesByGroupName(group.name()).size());
            if (group.maximalServices() <= getServicesByGroupName(group.name()).size()) {
                for (int i = 0; i < group.minimalServices(); i++) new MinecraftService(group).start();
            }
        });
    }

    public MinecraftService getServiceByName(String servicename) {
        return minecraftServiceCache.get(servicename);
    }

    public List<MinecraftService> getServicesByGroupName(String groupname) {
        return minecraftServices.stream().filter(service -> service.getDefaultServiceGroup().name().equals(groupname)).toList();
    }

    public List<MinecraftService> getServicesByGroupTitle(String groupTitle) {
        return minecraftServices.stream().filter(service -> service.getDefaultServiceGroup().groupTitle().equals(groupTitle)).toList();
    }

    public List<MinecraftService> getLobbyServices() {
        return minecraftServices.stream().filter(service -> service.getDefaultServiceGroup().serviceType().equals(ServiceType.Lobby)).toList();
    }

    public void addService(MinecraftService minecraftService) {
        minecraftServices.add(minecraftService);
        minecraftServiceCache.put(minecraftService.serviceName(), minecraftService);
        new DefaultJsonService(minecraftService);
    }

    public void removeService(MinecraftService minecraftService) {
        minecraftServices.remove(minecraftService);
        minecraftServiceCache.remove(minecraftService.serviceName(), minecraftService);
        new DefaultJsonService(minecraftService).delete();
    }

    public void addService(MinecraftService minecraftService, boolean bridge) {
        minecraftServices.add(minecraftService);
        minecraftServiceCache.put(minecraftService.serviceName(), minecraftService);
    }

    public void removeService(MinecraftService minecraftService, boolean bridge) {
        minecraftServices.remove(minecraftService);
        minecraftServiceCache.remove(minecraftService.serviceName(), minecraftService);
    }

    public int getFreeServiceId(DefaultServiceGroup defaultServiceGroup) {
        AtomicInteger count = new AtomicInteger(1);
        minecraftServices.forEach(service -> {
            if (service.getDefaultServiceGroup() == defaultServiceGroup && service.getServiceId() == count.get()) {
                count.getAndIncrement();
            }
        });

        return count.get();
    }

    @SneakyThrows
    public byte[] getByteArray(MinecraftService minecraftService) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(minecraftService);
        return out.toByteArray();
    }
}