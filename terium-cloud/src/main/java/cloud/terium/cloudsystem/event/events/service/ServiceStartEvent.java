package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutServiceStart;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceStartEvent extends Event {

    private final ICloudService cloudService;
    private final INode node;

    public ServiceStartEvent(ICloudService cloudService, INode node) {
        this.cloudService = cloudService;
        this.node = node;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceStart(cloudService, cloudService.getServiceGroup().getServiceGroupNode()));
    }
}