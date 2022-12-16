package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.ServiceState;

public record PacketPlayOutUpdateService(String servicename, boolean locked, ServiceState serviceState,
                                         int onlinePlayers, long usedMemory) implements Packet {
}