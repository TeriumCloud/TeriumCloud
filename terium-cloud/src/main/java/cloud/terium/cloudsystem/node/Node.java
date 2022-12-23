package cloud.terium.cloudsystem.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdown;
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
    private final TeriumClient client;

    public Node(String name, String key, InetSocketAddress address) {
        this.name = name;
        this.key = key;
        this.address = address;
        this.usedMemory = 0;
        this.maxMemory = 0;
        this.client = null;
        //this.client = TeriumFramework.createClient(address.getHostName(), address.getPort());
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
        if(client == null) return false;
        return client.getChannel().isActive();
    }

    @Override
    public long getUsedMemory() {
        return usedMemory;
    }

    @Override
    public long getMaxMemory() {
        return maxMemory;
    }

    @Override
    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    @Override
    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    @Override
    public void update() {
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutNodeUpdate(this, usedMemory, maxMemory));
    }

    @Override
    public void disconnect() {
        Logger.log("Trying to disconnect node '" + name + "'...", LogType.INFO);
        client.getChannel().disconnect();
        Logger.log("Successfully disconnected node '" + name + "'.", LogType.INFO);
    }

    @Override
    public void stop() {
        Logger.log("Trying to stop node '" + name + "'...", LogType.INFO);
        client.getChannel().writeAndFlush(new PacketPlayOutNodeShutdown(this));
        Logger.log("Successfully stopped node '" + name + "'.", LogType.INFO);
    }
}
