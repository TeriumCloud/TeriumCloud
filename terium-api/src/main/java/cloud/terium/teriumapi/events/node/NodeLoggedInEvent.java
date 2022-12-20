package cloud.terium.teriumapi.events.node;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import lombok.Getter;

@Getter
public class NodeLoggedInEvent extends Event {

    private final INode node;

    public NodeLoggedInEvent(INode node) {
        this.node = node;
    }
}