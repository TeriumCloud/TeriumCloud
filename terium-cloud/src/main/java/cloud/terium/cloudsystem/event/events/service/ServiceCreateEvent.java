package cloud.terium.cloudsystem.event.events.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.Getter;

import java.util.List;

@Getter
public class ServiceCreateEvent extends Event {

    private final String name;
    private final ICloudServiceGroup serviceGroup;
    private final List<ITemplate> templates;
    private final int maxPlayers;
    private final int memory;
    private final int serviceId;


    public ServiceCreateEvent(String name, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int maxPlayers, int memory, int serviceId) {
        this.name = name;
        this.serviceGroup = serviceGroup;
        this.templates = templates;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.serviceId = serviceId;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCreateService(name, serviceGroup, templates, maxPlayers, memory, serviceId));

    }
}