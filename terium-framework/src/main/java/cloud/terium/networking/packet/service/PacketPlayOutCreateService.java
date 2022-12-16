package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

public record PacketPlayOutCreateService(String name, ICloudServiceGroup serviceGroup, ITemplate template, int port,
                                         int maxPlayers, int memory, int serviceId,
                                         ServiceType cloudServiceType) implements Packet {
}