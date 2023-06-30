package cloud.terium.cloudsystem.cluster.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.node.PacketPlayOutNodeUpdate;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;

public class Node implements INode {

    private final String name;
    private final InetSocketAddress address;
    private long usedMemory;
    private long maxMemory;

    public Node(String name, InetSocketAddress address) {
        this.name = name;
        this.address = address;
        this.usedMemory = 0;
        this.maxMemory = 0;
    }

    public Node(String name, InetSocketAddress address, long maxMemory) {
        this.name = name;
        this.address = address;
        this.usedMemory = 0;
        this.maxMemory = maxMemory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public boolean isConnected() {
        if (ClusterStartup.getCluster().getNodeProvider().getClientFromNode(ClusterStartup.getCluster().getNodeProvider().getNodeByName(name).orElseGet(null)) == null)
            return false;
        return ClusterStartup.getCluster().getNodeProvider().getClientFromNode(ClusterStartup.getCluster().getNodeProvider().getNodeByName(name).orElseGet(null)).channel().isActive();
    }

    @Override
    public long getUsedMemory() {
        return usedMemory;
    }

    @Override
    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    @Override
    public long getMaxMemory() {
        return maxMemory;
    }

    @Override
    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    @Override
    public void update() {
        ClusterStartup.getCluster().getNetworking().sendPacket(new PacketPlayOutNodeUpdate(getName(), usedMemory, maxMemory));
    }
}
