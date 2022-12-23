package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;

public record PacketPlayOutServiceLock(ICloudService cloudService) implements Packet {
}