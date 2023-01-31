package cloud.terium.networking.packet.node;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;

public record PacketPlayOutNodeStarted(INode node) implements Packet {

    @Override
    public INode node() {
        return new INode() {
            @Override
            public String getName() {
                return node.getName();
            }

            @Override
            public String getKey() {
                return node.getKey();
            }

            @Override
            public InetSocketAddress getAddress() {
                return node.getAddress();
            }

            @Override
            public boolean isConnected() {
                return node.isConnected();
            }

            @Override
            public long getUsedMemory() {
                return node.getUsedMemory();
            }

            @Override
            public void setUsedMemory(long usedMemory) {
                node.setUsedMemory(usedMemory);
            }

            @Override
            public long getMaxMemory() {
                return node.getMaxMemory();
            }

            @Override
            public void setMaxMemory(long maxMemory) {
                node.setMaxMemory(maxMemory);
            }

            @Override
            public void update() {
                node.update();
            }

            @Override
            public void disconnect() {
                node.disconnect();
            }

            @Override
            public void stop() {
                node.stop();
            }
        };
    }
}