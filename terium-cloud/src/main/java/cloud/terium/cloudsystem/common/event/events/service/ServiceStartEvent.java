package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.service.PacketPlayOutServiceStart;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceStartEvent extends Event {

    private final String cloudService;
    private final String node;

    public ServiceStartEvent(String cloudService, String node) {
        this.cloudService = cloudService;
        this.node = node;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceStart(cloudService, cloudService));
    }
}