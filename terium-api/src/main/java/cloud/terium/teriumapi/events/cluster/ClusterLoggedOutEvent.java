package cloud.terium.teriumapi.events.cluster;

import cloud.terium.teriumapi.cluster.ICluster;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ClusterLoggedOutEvent extends Event {

    private final ICluster cluster;

    public ClusterLoggedOutEvent(ICluster cluster) {
        this.cluster = cluster;
    }
}