package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.service.PacketPlayOutServiceForceShutdown;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceForceStopEvent extends Event {

    private final String cloudService;

    public ServiceForceStopEvent(String cloudService) {
        this.cloudService = cloudService;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceForceShutdown(cloudService));
    }
}