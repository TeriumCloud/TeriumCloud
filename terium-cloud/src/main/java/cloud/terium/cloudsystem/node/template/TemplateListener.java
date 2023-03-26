package cloud.terium.cloudsystem.node.template;

import cloud.terium.cloudsystem.common.event.events.service.template.TemplateCreateEvent;
import cloud.terium.cloudsystem.common.event.events.service.template.TemplateDeleteEvent;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

public class TemplateListener implements Listener {

    @Subscribe
    public void handleTemplateCreate(TemplateCreateEvent event) {
        NodeStartup.getNode().getTemplateFactory().createTemplate(event.getName());
    }

    @Subscribe
    public void handleTemplateDelete(TemplateDeleteEvent event) {
        NodeStartup.getNode().getTemplateFactory().deleteTemplate(event.getTemplate());
    }
}