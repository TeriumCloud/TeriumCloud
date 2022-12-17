package cloud.terium.teriumapi.cluster;

import java.net.InetSocketAddress;

public interface IClusterFactory {

    /**
     * Use that methode to create a cluster.
     *
     * @param name String
     * @param key String
     * @param address InetSocketAddress
     */
    void createCluster(String name, String key, InetSocketAddress address);

    /**
     * Use that methode to delete a cluster by name.
     * @param name String
     */
    void deleteCluster(String name);
}