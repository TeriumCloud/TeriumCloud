package cloud.terium.cloudsystem.cluster.service;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.event.events.service.*;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.impl.CloudService;

public class CloudServiceListener implements Listener {

    @Subscribe
    public void handleServiceUpdate(ServiceUpdateEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> {
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
        if (event.getNode().getName().equals(ClusterStartup.getCluster().getThisNode().getName())) {
            switch (event.getType()) {
                case "group_only" ->
                        ClusterStartup.getCluster().getServiceFactory().createService(event.getServiceGroup());
                case "group_with_templates" ->
                        ClusterStartup.getCluster().getServiceFactory().createService(event.getServiceGroup(), event.getTemplates());
                case "full" ->
                        ClusterStartup.getCluster().getServiceFactory().createService(event.getName(), event.getServiceGroup(), event.getTemplates(), event.getServiceId(), event.getMaxPlayers(), event.getMemory());
                case "group_with_custom_name" ->
                        ClusterStartup.getCluster().getServiceFactory().createService(event.getName(), event.getServiceGroup());
                case "group_template_and_custom_name" ->
                        ClusterStartup.getCluster().getServiceFactory().createService(event.getName(), event.getServiceGroup(), event.getTemplates());
            }
        } else if (ClusterStartup.getCluster().isDebugMode())
            Logger.log("The service '§b" + (event.getServiceId() > 9 ? event.getName() + ClusterStartup.getCluster().getCloudConfig().splitter() + event.getServiceId() : event.getName() + ClusterStartup.getCluster().getCloudConfig().splitter() + "0" + event.getServiceId()) + "§f' is starting on node '§b" + event.getNode().getName() + "§f'.", LogType.INFO);
    }

    @Subscribe
    public void handleServiceForceStop(ServiceForceStopEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(ICloudService::forceShutdown);
    }

    @Subscribe
    public void handleServiceForceStop(ServiceStopEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(ICloudService::shutdown);
    }

    @Subscribe
    public void handleServiceLoggedIn(ServiceLoggedInEvent event) {
        if (ClusterStartup.getCluster().getThisNode().getName().equals(event.getNode()))
            Logger.log("Service '§b" + event.getCloudService() + "§f' successfully started.", LogType.INFO);
        else
            Logger.log("Service '§b" + event.getCloudService() + "§f' successfully started on node '§b" + event.getNode() + "§f'.", LogType.INFO);
    }

    @Subscribe
    public void handleServiceRemove(ServiceRemoveEvent event) {
        ClusterStartup.getCluster().getServiceProvider().removeService(ClusterStartup.getCluster().getServiceProvider().getServiceByName(event.getCloudService()).orElseGet(null));
    }

    @Subscribe
    public void handleServiceLock(ServiceLockEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(true));
    }

    @Subscribe
    public void handleServiceLock(ServiceUnlockEvent event) {
        ClusterStartup.getCluster().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(false));
    }
}