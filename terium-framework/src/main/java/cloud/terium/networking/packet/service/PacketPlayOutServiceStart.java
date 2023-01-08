package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;

public record PacketPlayOutServiceStart(ICloudService cloudService, INode node) implements Packet {
}