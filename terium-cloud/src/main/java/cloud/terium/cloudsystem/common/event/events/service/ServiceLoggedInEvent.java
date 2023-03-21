package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceLoggedInEvent extends Event {

    private final String cloudService;
    private final String node;

    public ServiceLoggedInEvent(String cloudService, String node) {
        this.cloudService = cloudService;
        this.node = node;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSuccessfullyServiceStarted(cloudService, node));
    }
}