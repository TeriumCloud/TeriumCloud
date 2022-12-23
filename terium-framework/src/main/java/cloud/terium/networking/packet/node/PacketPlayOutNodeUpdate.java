package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;

public record PacketPlayOutNodeUpdate(INode node, long usedMemory, long maxMemory) implements Packet {
}