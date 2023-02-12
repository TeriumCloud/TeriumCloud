package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutServiceLock;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceLockEvent extends Event {

    private final String cloudService;

    public ServiceLockEvent(String cloudService) {
        this.cloudService = cloudService;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceLock(cloudService));
    }
}