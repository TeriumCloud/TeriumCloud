package cloud.terium.cloudsystem.common.event.events.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class NodeLoggedInEvent extends Event {

    private final String node;

    public NodeLoggedInEvent(String node, String key) {
        this.node = node;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutNodeStarted(node, key));
    }
}
