package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.List;

public record PacketPlayOutService(List<ICloudService> cloudService) implements Packet {
}
