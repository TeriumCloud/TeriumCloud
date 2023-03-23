package cloud.terium.teriumapi.node;

import java.io.Serializable;
import java.net.InetSocketAddress;

public interface INode extends Serializable {

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
     * Get the used memory of the node.
     *
     * @return Long This returns the used memory of the node.
     */
    long getUsedMemory();

    /**
     * Set the used memory of the node.
     *
     * @param usedMemory
     */
    void setUsedMemory(long usedMemory);

    /**
     * Get the max memory of the node.
     *
     * @return Long This returns the max memory of the node.
     */
    long getMaxMemory();

    /**
     * Set the max memory of the node.
     *
     * @param maxMemory
     */
    void setMaxMemory(long maxMemory);

    /**
     * Update the node.
     */
    void update();

    /**
     * Use that methode to disconnect the node.
     */
    void disconnect();

    /**
     * Use that methode to stop the node.
     */
    void stop();
}