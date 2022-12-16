package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutSuccessfullyServiceShutdown(String servicename) implements Packet {
}