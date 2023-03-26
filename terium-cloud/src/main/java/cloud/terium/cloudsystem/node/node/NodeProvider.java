package cloud.terium.cloudsystem.node.node;

import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class NodeProvider implements INodeProvider {

    private final List<INode> cachedNodes;

    public NodeProvider() {
        this.cachedNodes = new CopyOnWriteArrayList<>();
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
