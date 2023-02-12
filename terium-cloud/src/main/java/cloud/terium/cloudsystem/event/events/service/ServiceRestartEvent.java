package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRestart;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceRestartEvent extends Event {

    private final String cloudService;

    public ServiceRestartEvent(String cloudService) {
        this.cloudService = cloudService;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceRestart(cloudService));
    }
}