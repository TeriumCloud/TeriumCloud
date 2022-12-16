package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.CloudServiceState;

public record PacketPlayOutUpdateService(String servicename, boolean locked, CloudServiceState serviceState,
                                         int onlinePlayers, long usedMemory) implements Packet {
}