package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutServiceForceShutdown(String servicename) implements Packet {
}