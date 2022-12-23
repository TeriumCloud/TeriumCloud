package cloud.terium.cloudsystem.node;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdown;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;

public class Node implements INode {

    private final String name;
    private final String key;
    private final InetSocketAddress address;
    private final TeriumClient client;

    public Node(String name, String key, InetSocketAddress address) {
        this.name = name;
        this.key = key;
        this.address = address;
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
        return client.getChannel().isActive();
    }

    @Override
    public void disconnect() {
        client.getChannel().disconnect();
    }

    @Override
    public void stop() {
        client.getChannel().writeAndFlush(new PacketPlayOutNodeShutdown(this));
    }
}
