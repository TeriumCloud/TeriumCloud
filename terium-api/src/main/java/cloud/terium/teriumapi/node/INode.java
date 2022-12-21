package cloud.terium.teriumapi.node;

import java.net.InetSocketAddress;

public interface INode {

    /**
     * Get the name of the node
     *
     * @return String
     */
    String getName();

    /**
     * Get the node key of the node
     *
     * @return String
     */
    String getKey();

    /**
     * Get the address of the node
     *
     * @return InetSocketAddress
     */
    InetSocketAddress getAddress();

    /**
     * Get if the node is the head of all nodes.
     *
     * @return Boolean This returns if the node is the head of all nodes. (true if yes else false)
     */
    boolean isHeadNode();

    /**
     * Set the current node to the head of all nodes.
     *
     * @param headNode Boolean
     */
    boolean setHeadNode(boolean headNode);
}