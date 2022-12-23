package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;

public record PacketPlayOutSuccessfullyServiceStarted(ICloudService cloudService) implements Packet {
}