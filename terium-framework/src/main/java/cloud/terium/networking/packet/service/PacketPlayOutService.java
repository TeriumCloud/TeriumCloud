package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;

public record PacketPlayOutService(ICloudService cloudService) implements Packet {
}
