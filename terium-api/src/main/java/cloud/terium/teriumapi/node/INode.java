package cloud.terium.teriumapi.node;

import java.net.InetSocketAddress;

public interface INode {

    /**
     * Get the name of a node
     *
     * @return String
     */
    String getName();

    /**
     * Get the address of a node
     *
     * @return
     */
    InetSocketAddress getAddress();
}