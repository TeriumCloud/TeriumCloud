package cloud.terium.cloudsystem.common.event.events.service.template;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateCreate;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class TemplateCreateEvent extends Event {

    private final String name;

    public TemplateCreateEvent(String name) {
        this.name = name;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateCreate(name));
    }
}