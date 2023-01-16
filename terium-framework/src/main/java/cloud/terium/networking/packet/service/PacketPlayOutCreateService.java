package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public record PacketPlayOutCreateService(String name, ICloudServiceGroup serviceGroup, List<ITemplate> templates,
                                         int maxPlayers, int memory, int serviceId) implements Packet {
}