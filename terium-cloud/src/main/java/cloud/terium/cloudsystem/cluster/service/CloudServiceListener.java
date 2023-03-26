package cloud.terium.cloudsystem.cluster.service;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.event.events.service.*;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.impl.CloudService;

import java.util.concurrent.ThreadLocalRandom;

public class CloudServiceListener implements Listener {

    @Subscribe
    public void handleServiceUpdate(ServiceUpdateEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> {
            cloudService.setServiceState(event.getServiceState());
            cloudService.setLocked(event.isLocked());
            cloudService.setUsedMemory((long) event.getMemory());
            cloudService.setOnlinePlayers(event.getPlayers());
            event.getPropertyCache().forEach(cloudService::addProperty);
        });
    }

    @Subscribe
    public void handleServiceAdd(ServiceAddEvent event) {
        ClusterStartup.getCluster().getServiceProvider().addService(new CloudService(event.getServiceName(), event.getServiceId(), event.getPort(), event.getMemory(),
                ClusterStartup.getCluster().getNodeProvider().getNodeByName(event.getNode()).orElseGet(null),
                ClusterStartup.getCluster().getServiceGroupProvider().getServiceGroupByName(event.getServiceGroup()).orElseGet(null),
                event.getTemplates().stream().map(s -> ClusterStartup.getCluster().getTemplateProvider().getTemplateByName(s).orElseGet(null)).toList(), event.getPropertyCache()));
    }

    @Subscribe
    public void handleServiceCreate(ServiceCreateEvent event) {
        if (event.getNode().getName().equals(ClusterStartup.getCluster().getThisNode().getName()))
            new cloud.terium.cloudsystem.cluster.service.CloudService(event.getTemplates(), event.getServiceGroup(), event.getServiceId() != -1 ? event.getServiceId() : ClusterStartup.getCluster().getServiceProvider().getFreeServiceId(event.getServiceGroup()),
                    event.getPort() != -1 ? event.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), event.getMaxPlayers(), event.getMemory()).start();
        else Logger.log("The service '" + event.getName() + "' is starting on node '" + event.getNode().getName() + "'.", LogType.INFO);
    }

    @Subscribe
    public void handleServiceForceStop(ServiceForceStopEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(ICloudService::forceShutdown);
    }

    @Subscribe
    public void handleServiceForceStop(ServiceStopEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(ICloudService::shutdown);
    }

    @Subscribe
    public void handleServiceLoggedIn(ServiceLoggedInEvent event) {
        if (ClusterStartup.getCluster().getThisNode().getName().equals(event.getNode()))
            Logger.log("Service '" + event.getCloudService() + "' successfully started.", LogType.INFO);
        else
            Logger.log("Service '" + event.getCloudService() + "' successfully started on node '" + event.getNode() + "'.", LogType.INFO);
    }

    @Subscribe
    public void handleServiceRemove(ServiceRemoveEvent event) {
        ClusterStartup.getCluster().getServiceProvider().removeService(ClusterStartup.getCluster().getServiceProvider().getCloudServiceByName(event.getCloudService()).orElseGet(null));
    }

    @Subscribe
    public void handleServiceLock(ServiceLockEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(true));
    }

    @Subscribe
    public void handleServiceLock(ServiceUnlockEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(false));
    }
}