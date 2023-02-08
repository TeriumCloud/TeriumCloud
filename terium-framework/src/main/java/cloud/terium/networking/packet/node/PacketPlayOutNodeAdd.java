package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.network.Packet;

import java.net.InetSocketAddress;

public record PacketPlayOutNodeAdd(String name, String key, InetSocketAddress address, long memory, boolean connected) implements Packet {
}