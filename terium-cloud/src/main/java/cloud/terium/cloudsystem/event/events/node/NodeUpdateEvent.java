package cloud.terium.cloudsystem.event.events.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.node.PacketPlayOutNodeUpdate;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
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
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutNodeUpdate(node, usedMemory, maxMemory));
    }
}
