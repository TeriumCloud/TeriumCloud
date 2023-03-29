package cloud.terium.cloudsystem.node.service;

import cloud.terium.cloudsystem.common.event.events.service.*;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.impl.CloudService;

public class CloudServiceListener implements Listener {

    @Subscribe
    public void handleServiceUpdate(ServiceUpdateEvent event) {
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> {
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
        if(event.getNode().getName().equals(NodeStartup.getNode().getThisNode().getName()))
            switch (event.getType()) {
            case "group_only" -> NodeStartup.getNode().getServiceFactory().createService(event.getServiceGroup());
            case "group_with_templates" -> NodeStartup.getNode().getServiceFactory().createService(event.getServiceGroup(), event.getTemplates());
            case "full" -> NodeStartup.getNode().getServiceFactory().createService(event.getName(), event.getServiceGroup(), event.getTemplates(), event.getServiceId(), event.getMaxPlayers(), event.getMemory());
            case "group_with_custom_name" -> NodeStartup.getNode().getServiceFactory().createService(event.getName(), event.getServiceGroup());
            case "group_template_and_custom_name" -> NodeStartup.getNode().getServiceFactory().createService(event.getName(), event.getServiceGroup(), event.getTemplates());
        }
    }

    @Subscribe
    public void handleServiceForceStop(ServiceForceStopEvent event) {
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(ICloudService::forceShutdown);
    }

    @Subscribe
    public void handleServiceForceStop(ServiceStopEvent event) {
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(ICloudService::shutdown);
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
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> NodeStartup.getNode().getServiceProvider().removeService(cloudService));
    }

    @Subscribe
    public void handleServiceLock(ServiceLockEvent event) {
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(true));
    }

    @Subscribe
    public void handleServiceLock(ServiceUnlockEvent event) {
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> cloudService.setLocked(false));
    }
}