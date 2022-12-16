package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

public record PacketPlayOutCreateService(String name, ICloudServiceGroup serviceGroup, ITemplate template, int port,
                                         int maxPlayers, int memory, int serviceId,
                                         CloudServiceType cloudServiceType) implements Packet {
}