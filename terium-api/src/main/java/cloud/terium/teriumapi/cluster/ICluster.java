package cloud.terium.teriumapi.cluster;

import java.net.InetSocketAddress;

public interface ICluster {

    /**
     * Get the name of the cluster
     *
     * @return String
     */
    String getName();

    /**
     * Get the cluster key of the cluster
     * @return String
     */
    String getClusterKey();

    /**
     * Get the address of the node
     *
     * @return InetSocketAddress
     */
    InetSocketAddress getAddress();
}