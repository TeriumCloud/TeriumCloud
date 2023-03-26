package cloud.terium.cloudsystem.node.node;

import cloud.terium.networking.packet.node.PacketPlayOutCreateNode;
import cloud.terium.networking.packet.node.PacketPlayOutDeleteNode;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeFactory;

import java.net.InetSocketAddress;

public class NodeFactory implements INodeFactory {

    @Override
    public void createNode(String name, String key, InetSocketAddress address) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateNode(name, key, address));
    }

    @Override
    public void deleteNode(INode node) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutDeleteNode(node.getName()));
    }
}
