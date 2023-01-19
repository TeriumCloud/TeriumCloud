package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;

public record PacketPlayOutDeleteNode(INode node) implements Packet {
}
