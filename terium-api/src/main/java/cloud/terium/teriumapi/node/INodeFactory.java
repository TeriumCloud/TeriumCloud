package cloud.terium.teriumapi.node;

import java.net.InetSocketAddress;

public interface INodeFactory {

    /**
     * Use that methode to create a node.
     *
     * @param name    String
     * @param key     String
     * @param address InetSocketAddress
     */
    void createNode(String name, String key, InetSocketAddress address);

    /**
     * Use that methode to delete a node by name.
     *
     * @param node INode
     */
    void deleteNode(INode node);
}