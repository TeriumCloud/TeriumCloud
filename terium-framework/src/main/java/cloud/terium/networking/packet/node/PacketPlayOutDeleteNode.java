package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.node.INode;

import java.util.Optional;

public record PacketPlayOutDeleteNode(String node) implements Packet {

    public Optional<INode> parsedNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node);
    }
}