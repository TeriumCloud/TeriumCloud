package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.pipe.Packet;

import java.net.InetSocketAddress;

public record PacketPlayOutCreateNode(String name, String key, InetSocketAddress address) implements Packet {
}