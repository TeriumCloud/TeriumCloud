package cloud.terium.cloudsystem.node;

import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeFactory;

import java.net.InetSocketAddress;

public class NodeFactory implements INodeFactory {

    @Override
    public void createNode(String name, String key, InetSocketAddress address) {
        // Todo: Implement event
    }

    @Override
    public void deleteNode(INode node) {
        // Todo: Implement event
    }
}