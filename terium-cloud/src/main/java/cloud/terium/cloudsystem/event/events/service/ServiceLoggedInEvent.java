package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceLoggedInEvent extends Event {

    private final String cloudService;
    private final String node;

    public ServiceLoggedInEvent(String cloudService, String node) {
        this.cloudService = cloudService;
        this.node = node;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutSuccessfullyServiceStarted(cloudService, node));
    }
}