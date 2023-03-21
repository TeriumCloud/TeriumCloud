package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRemove;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceRemoveEvent extends Event {

    private final String cloudService;

    public ServiceRemoveEvent(String cloudService) {
        this.cloudService = cloudService;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(cloudService));
    }
}