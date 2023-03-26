package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;
import java.util.Optional;

public record PacketPlayOutNodeStarted(String node, InetSocketAddress address, long maxMemory, String masterKey) implements Packet {

    public Optional<INode> parsedNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node);
    }
}