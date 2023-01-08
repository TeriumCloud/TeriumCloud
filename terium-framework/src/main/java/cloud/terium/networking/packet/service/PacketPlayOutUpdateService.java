package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;

import java.util.HashMap;

public record PacketPlayOutUpdateService(ICloudService cloudService, HashMap<String, Object> propertyMap,
                                         boolean locked, ServiceState serviceState,
                                         int onlinePlayers, long usedMemory) implements Packet {
}