package cloud.terium.cloudsystem.common.event.events.service.template;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateDelete;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class TemplateDeleteEvent extends Event {

    private final String template;

    public TemplateDeleteEvent(String template) {
        this.template = template;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateDelete(template));
    }
}