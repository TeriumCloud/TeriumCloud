package cloud.terium.cloudsystem.common.event.events.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdown;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import lombok.Getter;

@Getter
public class NodeShutdownedEvent extends Event {

    private final INode node;

    public NodeShutdownedEvent(INode node) {
        this.node = node;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutNodeShutdown(node.getName()));
    }
}
