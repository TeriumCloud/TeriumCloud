package cloud.terium.module.dockerizedservices.service;

import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.event.events.service.ServiceCreateEvent;
import cloud.terium.module.dockerizedservices.TeriumDockerizedServices;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

public class DockerizedServiceListener implements Listener {

    @Subscribe
    public void handleServiceCreate(ServiceCreateEvent event) {
        if (TeriumDockerizedServices.getInstance().getServiceFactory().containsServiceGroup(event.getServiceGroup())) {
            if (event.getNode().getName().equals(TeriumAPI.getTeriumAPI().getProvider().getThisNode().getName())) {
                switch (event.getType()) {
                    case "group_only" ->
                            TeriumDockerizedServices.getInstance().getServiceFactory().createService(event.getServiceGroup());
                    case "group_with_templates" ->
                            TeriumDockerizedServices.getInstance().getServiceFactory().createService(event.getServiceGroup(), event.getTemplates());
                    case "full" ->
                            TeriumDockerizedServices.getInstance().getServiceFactory().createService(event.getName(), event.getServiceGroup(), event.getTemplates(), event.getServiceId(), event.getMaxPlayers(), event.getMemory(), event.getPropertyCache());
                    case "group_with_custom_name" ->
                            TeriumDockerizedServices.getInstance().getServiceFactory().createService(event.getName(), event.getServiceGroup());
                    case "group_template_and_custom_name" ->
                            TeriumDockerizedServices.getInstance().getServiceFactory().createService(event.getName(), event.getServiceGroup(), event.getTemplates());
                }
            } else
                Logger.log("The service '§b" + (event.getServiceId() > 9 ? event.getName() + "-" + event.getServiceId() : event.getName() + "-" + "0" + event.getServiceId()) + "§f' is starting on node '§b" + event.getNode().getName() + "§f'.", LogType.INFO);
        }
    }
}
