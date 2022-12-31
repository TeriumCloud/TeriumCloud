package cloud.terium.cloudsystem.event.events.template;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.PacketPlayOutTemplateDelete;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.Getter;

@Getter
public class TemplateDeleteEvent extends Event {

    private final ITemplate template;

    public TemplateDeleteEvent(ITemplate template) {
        this.template = template;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutTemplateDelete(template));
    }
}