package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutServiceAdd(String serviceName, String serviceGroup, String template, int serviceId,
                                      int port) implements Packet {
}