package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutServiceAdd;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceAddEvent extends Event {

    private final ICloudService cloudService;

    public ServiceAddEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceAdd(cloudService, cloudService.getServiceGroup(), cloudService.getTemplates(), cloudService.getServiceId(), cloudService.getPort()));
    }
}