package cloud.terium.minestom.cloud.impl.node;

import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NodeProvider implements INodeProvider {

    private final List<INode> cachedNodes;

    public NodeProvider() {
        this.cachedNodes = new ArrayList<>();
    }

    @Override
    public Optional<INode> getNodeByName(String name) {
        return cachedNodes.stream().filter(node -> node.getName().equals(name)).findAny();
    }

    @Override
    public List<INode> getAllNodes() {
        return cachedNodes;
    }
}
