package cloud.terium.teriumapi.events.node;

import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class NodeLoggedOutEvent extends Event {

    private final INode node;

    public NodeLoggedOutEvent(INode node) {
        this.node = node;
    }
}