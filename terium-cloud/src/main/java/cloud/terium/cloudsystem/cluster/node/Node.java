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

    @Override
    public void connect() {
        try {
            this.client = TeriumFramework.createClient(address.getAddress().getHostAddress(), address.getPort());
            ClusterStartup.getCluster().getNodeProvider().addClientToNode(this, client);
            Logger.log("Connected to node '" + name + "'.", LogType.INFO);
            ClusterStartup.getCluster().getNetworking().sendPacket(new PacketPlayOutNodeStarted(getName()));
        } catch (Exception exception) {
            this.client = null;
            Logger.log("Connection to node '" + name + "' with ip '" + address.getAddress().getHostAddress() + ":" + address.getPort() + "' can't be created. (" + exception.getMessage() + ")", LogType.ERROR);
        }
    }

    @Override
    public void disconnect() {
        Logger.log("Trying to disconnect node '" + name + "'...", LogType.INFO);
        client.getChannel().disconnect();
        client.getChannel().writeAndFlush(new PacketPlayOutNodeShutdowned(getName()));
        Logger.log("Successfully disconnected node '" + name + "'.", LogType.INFO);
    }

    @Override
    public void stop() {
        Logger.log("Trying to stop node '" + name + "'...", LogType.INFO);
        client.getChannel().writeAndFlush(new PacketPlayOutNodeShutdown(getName()));
        Logger.log("Successfully stopped node '" + name + "'.", LogType.INFO);
    }
}
