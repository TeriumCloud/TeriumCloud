package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutServiceShutdown;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceStopEvent extends Event {

    private final ICloudService cloudService;
    private final INode node;

    public ServiceStopEvent(ICloudService cloudService, INode node) {
        this.cloudService = cloudService;
        this.node = node;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceShutdown(cloudService));
    }
}