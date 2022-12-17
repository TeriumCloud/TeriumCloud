package cloud.terium.teriumapi.cluster;

import java.util.List;

public interface IClusterProvider {

    /**
     * Get the name of the cluster
     *
     * @return String
     */
    List<ICluster> getAllClusters();

    /**
     *
     * @param name String
     * @return ICluster
     */
    ICluster getClusterByName(String name);
}