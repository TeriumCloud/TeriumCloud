package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutSuccessfullyServiceStarted(String servicename) implements Packet {
}