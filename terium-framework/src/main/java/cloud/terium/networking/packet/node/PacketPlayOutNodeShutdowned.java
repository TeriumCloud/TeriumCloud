package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;

public record PacketPlayOutNodeShutdowned(INode node) implements Packet {
}