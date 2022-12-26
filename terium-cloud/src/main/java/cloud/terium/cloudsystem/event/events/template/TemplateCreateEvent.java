package cloud.terium.cloudsystem.event.events.template;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.PacketPlayOutTemplateCreate;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class TemplateCreateEvent extends Event {

    private final String name;

    public TemplateCreateEvent(String name) {
        this.name = name;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutTemplateCreate(name));
    }
}