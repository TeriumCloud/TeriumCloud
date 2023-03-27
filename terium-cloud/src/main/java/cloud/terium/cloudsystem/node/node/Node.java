package cloud.terium.cloudsystem.node.node;

import cloud.terium.networking.packet.node.PacketPlayOutNodeUpdate;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;

public class Node implements INode {

    private final String name;
    private final String key;
    private final InetSocketAddress address;
    private long usedMemory;
    private long maxMemory;
    private boolean connected;

    public Node(String name, String key, InetSocketAddress address, long maxMemory, boolean connected) {
        this.name = name;
        this.key = key;
        this.address = address;
        this.usedMemory = 0;
        this.maxMemory = 0;
        this.connected = connected;
    }

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
        return connected;
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
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutNodeUpdate(getName(), usedMemory, maxMemory));
    }
}
