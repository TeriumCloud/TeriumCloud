package cloud.terium.cloudsystem.node.service;

import cloud.terium.cloudsystem.common.event.events.service.*;
import cloud.terium.cloudsystem.node.utils.Logger;
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
        NodeStartup.getNode().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> {
            cloudService.setServiceState(event.getServiceState());
            cloudService.setLocked(event.isLocked());
            cloudService.setUsedMemory((long) event.getMemory());
            cloudService.setOnlinePlayers(event.getPlayers());
            event.getPropertyCache().forEach(cloudService::addProperty);
        });
    }

    @Subscribe
    public void handleServiceAdd(ServiceAddEvent event) {
        NodeStartup.getNode().getServiceProvider().addService(new CloudService(event.getServiceName(), event.getServiceId(), event.getPort(), event.getMemory(),
                NodeStartup.getNode().getNodeProvider().getNodeByName(event.getNode()).orElseGet(null),
                NodeStartup.getNode().getServiceGroupProvider().getServiceGroupByName(event.getServiceGroup()).orElseGet(null),
                event.getTemplates().stream().map(s -> NodeStartup.getNode().getTemplateProvider().getTemplateByName(s).orElseGet(null)).toList(), event.getPropertyCache()));
    }

    @Subscribe
    public void handleServiceCreate(ServiceCreateEvent event) {
        new cloud.terium.cloudsystem.node.service.CloudService(event.getTemplates(), event.getServiceGroup(), event.getServiceId() != -1 ? event.getServiceId() : NodeStartup.getNode().getServiceProvider().getFreeServiceId(event.getServiceGroup()),
                event.getPort() != -1 ? event.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), event.getMaxPlayers(), event.getMemory()).start();
    }

    @Subscribe
    public void handleServiceForceStop(ServiceForceStopEvent event) {
        NodeStartup.getNode().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(ICloudService::forceShutdown);
    }

    @Subscribe
    public void handleServiceForceStop(ServiceStopEvent event) {
        NodeStartup.getNode().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(ICloudService::shutdown);
    }

    @Subscribe
    public void handleServiceLoggedIn(ServiceLoggedInEvent event) {
        if (NodeStartup.getNode().getThisNode().getName().equals(event.getNode()))
            Logger.log("Service '" + event.getCloudService() + "' successfully started.", LogType.INFO);
        else
            Logger.log("Service '" + event.getCloudService() + "' successfully started on node '" + event.getNode() + "'.", LogType.INFO);
    }

    @Subscribe
    public void handleServiceRemove(ServiceRemoveEvent event) {
        NodeStartup.getNode().getServiceProvider().getAllCloudServices().remove(NodeStartup.getNode().getServiceProvider().getCloudServiceByName(event.getCloudService()));
    }

    @Subscribe
    public void handleServiceLock(ServiceLockEvent event) {
        NodeStartup.getNode().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(true));
    }

    @Subscribe
    public void handleServiceLock(ServiceUnlockEvent event) {
        NodeStartup.getNode().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(false));
    }
}