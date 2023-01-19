package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;

import java.util.HashMap;

public record PacketPlayOutUpdateService(ICloudService cloudService) implements Packet {
}