package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

@Getter
public class ServiceCreateEvent extends Event {

    private final ICloudService cloudService;

    public ServiceCreateEvent(ICloudService cloudService) {
        this.cloudService = cloudService;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCreateService(cloudService.getServiceName(), cloudService.getServiceGroup(), cloudService.getTemplates(), cloudService.getPort(), cloudService.getMaxPlayers(), cloudService.getMaxMemory(), cloudService.getServiceId(), cloudService.getServiceType()));
    }
}