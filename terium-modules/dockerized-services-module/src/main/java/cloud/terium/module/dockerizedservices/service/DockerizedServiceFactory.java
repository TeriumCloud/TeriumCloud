package cloud.terium.module.dockerizedservices.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DockerizedServiceFactory implements ICloudServiceFactory {

    private final List<ICloudServiceGroup> bindedServiceGroups = new LinkedList<>();

    public void startKeepAliveCheckForServices() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream().filter(cloudService -> cloudService instanceof DockerizedService).forEach(cloudService -> {
                    if(cloudService instanceof DockerizedService dockerizedService) {
                        if(!dockerizedService.alive())
                            dockerizedService.shutdown();
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    public void createService(ICloudServiceGroup serviceGroup) {
        new DockerizedService(serviceGroup).start();
    }

    @Override
    public void createService(ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        new DockerizedService(serviceGroup, templates).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory) {
        new DockerizedService(serviceName, templates, serviceGroup, serviceGroup.getServiceType(), serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), maxPlayers, memory).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory, HashMap<String, Object> hashMap) {
        new DockerizedService(serviceName, templates, serviceGroup, serviceGroup.getServiceType(), serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), maxPlayers, memory, hashMap).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup) {
        new DockerizedService(serviceName, serviceGroup.getTemplates(), serviceGroup, serviceGroup.getServiceType(), TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory()).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        new DockerizedService(serviceName, templates, serviceGroup, serviceGroup.getServiceType(), TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory()).start();
    }

    @Override
    public boolean containsServiceGroup(ICloudServiceGroup serviceGroup) {
        return bindedServiceGroups.contains(serviceGroup);
    }

    @Override
    public void bindServiceGroup(ICloudServiceGroup serviceGroup) {
        this.bindedServiceGroups.add(serviceGroup);
    }

    @Override
    public void unbindServiceGroup(ICloudServiceGroup serviceGroup) {
        this.bindedServiceGroups.remove(serviceGroup);
    }
}