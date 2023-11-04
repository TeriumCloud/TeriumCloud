package cloud.terium.cloudsystem.node.service;

import cloud.terium.cloudsystem.common.event.events.service.*;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.impl.CloudService;

import java.util.concurrent.ThreadLocalRandom;

public class CloudServiceListener implements Listener {

    @Subscribe
    public void handleServiceUpdate(ServiceUpdateEvent event) {
        NodeStartup.getNode().getServiceProvider().getServiceByName(event.getCloudService()).ifPresent(cloudService -> {
            cloudService.setServiceState(event.getServiceState());
            cloudService.setLocked(event.isLocked());
            cloudService.setUsedMemory((long) event.getMemory());
            cloudService.setOnlinePlayers(event.getPlayers());
            cloudService.getPropertyMap().clear();
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
        if(TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().containsServiceGroup(event.getServiceGroup())) {
            if (event.getNode().getName().equals(NodeStartup.getNode().getThisNode().getName()))
                switch (event.getType()) {
                    case "group_only" -> new cloud.terium.cloudsystem.node.service.CloudService(event.getServiceGroup()).start();
                    case "group_with_templates" -> new cloud.terium.cloudsystem.node.service.CloudService(event.getServiceGroup(), event.getTemplates()).start();
                    case "full" -> new cloud.terium.cloudsystem.node.service.CloudService(event.getName(), event.getTemplates(), event.getServiceGroup(), event.getServiceGroup().getServiceType(), event.getServiceId(), ThreadLocalRandom.current().nextInt(20000, 50000), event.getMaxPlayers(), event.getMemory(), event.getPropertyCache()).start();
                    case "group_with_custom_name" -> new cloud.terium.cloudsystem.node.service.CloudService(event.getName(), event.getServiceGroup().getTemplates(), event.getServiceGroup(), event.getServiceGroup().getServiceType(), event.getServiceId(), event.getServiceGroup().hasPort() ? event.getServiceGroup().getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), event.getMaxPlayers(), event.getMemory()).start();
                    case "group_template_and_custom_name" -> new cloud.terium.cloudsystem.node.service.CloudService(event.getName(), event.getTemplates(), event.getServiceGroup(), event.getServiceGroup().getServiceType(), event.getServiceId(), event.getServiceGroup().hasPort() ? event.getServiceGroup().getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), event.getMaxPlayers(), event.getMemory()).start();
                }
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
            Logger.log("Service '§b" + event.getCloudService() + "§f' successfully started.", LogType.INFO);
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