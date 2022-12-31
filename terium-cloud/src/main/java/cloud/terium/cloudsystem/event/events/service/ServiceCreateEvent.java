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
    private final int port;
    private final int maxPlayers;
    private final int memory;
    private final int serviceId;
    private final ServiceType cloudServiceType;


    public ServiceCreateEvent(String name, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int port, int maxPlayers, int memory, int serviceId, ServiceType cloudServiceType) {
        this.name = name;
        this.serviceGroup = serviceGroup;
        this.templates = templates;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.serviceId = serviceId;
        this.cloudServiceType = cloudServiceType;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCreateService(name, serviceGroup, templates, port, maxPlayers, memory, serviceId, cloudServiceType));

    }
}