package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutServiceShutdown;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceStopEvent extends Event {

    private final String cloudService;
    private final String node;

    public ServiceStopEvent(String cloudService, String node) {
        this.cloudService = cloudService;
        this.node = node;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceShutdown(cloudService, node));
    }
}