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
     * Get if the node is connected with the current node.
     *
     * @return Boolean This returns if the node is connected. (true if yes else false)
     */
    boolean isConnected();

    /**
     * Use that methode to disconnect the node.
     */
    void disconnect();

    /**
     * Use that methode to stop the node.
     */
    void stop();
}