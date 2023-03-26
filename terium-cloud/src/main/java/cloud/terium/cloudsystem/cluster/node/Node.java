package cloud.terium.cloudsystem.cluster.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.networking.TeriumFramework;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdown;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdowned;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.networking.packet.node.PacketPlayOutNodeUpdate;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

public class Node implements INode {

    private final String name;
    private final String key;
    private final InetSocketAddress address;
    private long usedMemory;
    private long maxMemory;
    private TeriumClient client;

    public Node(String name, String key, InetSocketAddress address) {
        this.name = name;
        this.key = key;
        this.address = address;
        this.usedMemory = 0;
        this.maxMemory = 0;
    }

    public Node(String name, String key, String address) {
        this.name = name;
        this.key = key;
        this.address = new InetSocketAddress(address, ThreadLocalRandom.current().nextInt(2000, 6000));
        this.usedMemory = 0;
        this.maxMemory = 0;
    }

    public Node(String name, String key, InetSocketAddress address, long maxMemory) {
        this.name = name;
        this.key = key;
        this.address = address;
        this.usedMemory = 0;
        this.maxMemory = maxMemory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public boolean isConnected() {
        if (client == null) return false;
        return client.getChannel().isActive();
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
