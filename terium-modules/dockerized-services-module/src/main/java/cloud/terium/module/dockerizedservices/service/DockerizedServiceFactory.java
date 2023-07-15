package cloud.terium.module.dockerizedservices.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DockerizedServiceFactory implements ICloudServiceFactory {

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
}