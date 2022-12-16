package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutServiceRestart(String servicename) implements Packet {
}