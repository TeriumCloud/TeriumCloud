package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.pipe.Packet;

import java.net.InetSocketAddress;

public record PacketPlayOutNodeAdd(String name, InetSocketAddress address, long memory,
                                   boolean connected) implements Packet {
}