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
}