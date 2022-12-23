package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public record PacketPlayOutServiceAdd(ICloudService cloudService, ICloudServiceGroup serviceGroup, List<ITemplate> template, int serviceId,
                                      int port) implements Packet {
}