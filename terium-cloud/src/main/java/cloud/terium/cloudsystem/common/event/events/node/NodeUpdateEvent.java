package cloud.terium.cloudsystem.common.event.events.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.node.PacketPlayOutNodeUpdate;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class NodeUpdateEvent extends Event {

    private final String node;
    private final long usedMemory;
    private final long maxMemory;

    public NodeUpdateEvent(String node, long usedMemory, long maxMemory) {
        this.node = node;
        this.usedMemory = usedMemory;
        this.maxMemory = maxMemory;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutNodeUpdate(node, usedMemory, maxMemory));
    }
}
