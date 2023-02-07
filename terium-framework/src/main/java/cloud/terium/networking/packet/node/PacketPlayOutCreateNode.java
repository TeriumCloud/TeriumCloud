package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.network.Packet;

import java.net.InetSocketAddress;

public record PacketPlayOutCreateNode(String name, String key, InetSocketAddress address) implements Packet {
}