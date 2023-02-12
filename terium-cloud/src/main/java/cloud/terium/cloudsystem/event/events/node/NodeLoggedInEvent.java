package cloud.terium.cloudsystem.event.events.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class NodeLoggedInEvent extends Event {

    private final String node;

    public NodeLoggedInEvent(String node) {
        this.node = node;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutNodeStarted(node));
    }
}
