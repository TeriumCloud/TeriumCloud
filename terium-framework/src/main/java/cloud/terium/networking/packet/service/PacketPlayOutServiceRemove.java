package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutServiceRemove(String servicename) implements Packet {
}