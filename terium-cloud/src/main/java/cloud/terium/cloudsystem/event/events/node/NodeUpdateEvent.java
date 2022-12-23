package cloud.terium.cloudsystem.event.events.node;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import lombok.Getter;

@Getter
public class NodeUpdateEvent extends Event {

    private final INode node;

    public NodeUpdateEvent(INode node) {
        this.node = node;
    }
}
