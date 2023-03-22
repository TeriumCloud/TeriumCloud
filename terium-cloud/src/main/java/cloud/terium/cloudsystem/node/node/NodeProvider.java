package cloud.terium.cloudsystem.node.node;

import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Getter
public class NodeProvider implements INodeProvider {

    private final HashMap<String, INode> nodes;
    private final HashMap<INode, TeriumClient> nodeClients;

    public NodeProvider() {
        Logger.log("Loaded node-provider", LogType.INFO);
        this.nodes = new LinkedHashMap<>();
        this.nodeClients = new HashMap<>();
    }

    public void registerNode(INode node) {
        nodes.put(node.getName(), node);
    }

    public void registerNodes() {
        Logger.log("Trying to load all nodes from config.", LogType.INFO);
        registerNode(NodeStartup.getNode().getThisNode());
    }

    @Override
    public List<INode> getAllNodes() {
        return nodes.values().stream().toList();
    }

    @Override
    public Optional<INode> getNodeByName(String name) {
        return Optional.ofNullable(nodes.get(name));
    }
}