package cloud.terium.cloudsystem.common.event.events.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.net.InetSocketAddress;

@Getter
public class NodeLoggedInEvent extends Event {

    private final String node;
    private final InetSocketAddress address;
    private final long maxMemory;
    private final String masterKey;

    public NodeLoggedInEvent(String node, InetSocketAddress address, long maxMemory, String masterKey) {
        this.node = node;
        this.address = address;
        this.maxMemory = maxMemory;
        this.masterKey = masterKey;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutNodeStarted(node, address, maxMemory, masterKey));
    }
}
