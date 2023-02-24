package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.event.events.service.ServiceAddEvent;
import cloud.terium.cloudsystem.event.events.service.ServiceCreateEvent;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.service.impl.CloudService;

import java.util.concurrent.ThreadLocalRandom;

public class CloudServiceListener implements Listener {

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
}