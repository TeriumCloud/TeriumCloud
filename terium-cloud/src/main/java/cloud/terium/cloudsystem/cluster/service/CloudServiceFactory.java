package cloud.terium.cloudsystem.cluster.service;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CloudServiceFactory implements ICloudServiceFactory {

    @Override
    public void createService(ICloudServiceGroup serviceGroup) {
        new CloudService(serviceGroup).start();
    }

    @Override
    public void createService(ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        new CloudService(serviceGroup, templates).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory) {
        new CloudService(serviceName, templates, serviceGroup, serviceGroup.getServiceType(), serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), maxPlayers, memory).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory, HashMap<String, Object> hashMap) {
        new CloudService(serviceName, templates, serviceGroup, serviceGroup.getServiceType(), serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), maxPlayers, memory, hashMap).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup) {
        new CloudService(serviceName, serviceGroup.getTemplates(), serviceGroup, serviceGroup.getServiceType(), ClusterStartup.getCluster().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory()).start();
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        new CloudService(serviceName, templates, serviceGroup, serviceGroup.getServiceType(), ClusterStartup.getCluster().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory()).start();
    }
}
