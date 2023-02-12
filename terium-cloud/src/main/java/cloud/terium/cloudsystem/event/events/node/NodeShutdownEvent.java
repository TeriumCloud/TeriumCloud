package cloud.terium.cloudsystem.event.events.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdown;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class NodeShutdownEvent extends Event {

    private final String node;

    public NodeShutdownEvent(String node) {
        this.node = node;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutNodeShutdown(node));
    }
}
