package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.event.events.service.*;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.impl.CloudService;

import java.util.concurrent.ThreadLocalRandom;

public class CloudServiceListener implements Listener {

    @Subscribe
    public void handleServiceUpdate(ServiceUpdateEvent event) {
        TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> {
            cloudService.setServiceState(event.getServiceState());
            cloudService.setLocked(event.isLocked());
            cloudService.setUsedMemory((long) event.getMemory());
            cloudService.setOnlinePlayers(event.getPlayers());
            event.getPropertyCache().forEach(cloudService::addProperty);
        });
    }

    @Subscribe
    public void handleServiceAdd(ServiceAddEvent event) {
        TeriumCloud.getTerium().getServiceProvider().addService(new CloudService(event.getServiceName(), event.getServiceId(), event.getPort(), event.getMemory(),
                TeriumCloud.getTerium().getNodeProvider().getNodeByName(event.getNode()).orElseGet(null),
                TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(event.getServiceGroup()).orElseGet(null),
                event.getTemplates().stream().map(s -> TeriumCloud.getTerium().getTemplateProvider().getTemplateByName(s).orElseGet(null)).toList(), event.getPropertyCache()));
    }

    @Subscribe
    public void handleServiceCreate(ServiceCreateEvent event) {
        new cloud.terium.cloudsystem.service.CloudService(event.getTemplates(), event.getServiceGroup(), event.getServiceId() != -1 ? event.getServiceId() : TeriumCloud.getTerium().getServiceProvider().getFreeServiceId(event.getServiceGroup()),
                event.getPort() != -1 ? event.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), event.getMaxPlayers(), event.getMemory()).start();
    }

    @Subscribe
    public void handleServiceForceStop(ServiceForceStopEvent event) {
        TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(ICloudService::forceShutdown);
    }

    @Subscribe
    public void handleServiceLoggedIn(ServiceLoggedInEvent event) {
        if(TeriumCloud.getTerium().getThisNode().getName().equals(event.getNode())) Logger.log("Service '" + event.getCloudService() + "' successfully started.", LogType.INFO);
        else Logger.log("Service '" + event.getCloudService() + "' successfully started on node '" + event.getNode() + "'.", LogType.INFO);
    }

    @Subscribe
    public void handleServiceRemove(ServiceRemoveEvent event) {
        TeriumCloud.getTerium().getServiceProvider().getAllCloudServices().remove(TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(event.getCloudService()));
    }

    @Subscribe
    public void handleServiceLock(ServiceLockEvent event) {
        TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(true));
    }

    @Subscribe
    public void handleServiceLock(ServiceUnlockEvent event) {
        TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(false));
    }
}