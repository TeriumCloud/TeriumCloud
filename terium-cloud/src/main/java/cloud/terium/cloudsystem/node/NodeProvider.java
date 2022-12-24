package cloud.terium.cloudsystem.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.client.TeriumClient;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

@Getter
public class NodeProvider implements INodeProvider {

    private final HashMap<String, INode> nodes;
    private final HashMap<INode, TeriumClient> nodeClients;

    public NodeProvider() {
        this.nodes = new HashMap<>();
        this.nodeClients = new HashMap<>();
        TeriumCloud.getTerium().getCloudConfig().nodes().forEach(jsonElement -> registerNode(new Node(jsonElement.getAsJsonObject().get("name").getAsString(), jsonElement.getAsJsonObject().get("key").getAsString(),
                new InetSocketAddress(jsonElement.getAsJsonObject().get("ip").getAsString(), jsonElement.getAsJsonObject().get("port").getAsInt()))));
    }

    public void registerNode(INode node) {
        nodes.put(node.getName(), node);
    }

    @Override
    public List<INode> getAllNodes() {
        return nodes.values().stream().toList();
    }

    @Override
    public INode getNodeByName(String name) {
        return nodes.get(name);
    }

    public void addClientToNode(INode node, TeriumClient client) {
        nodeClients.put(node, client);
    }

    public TeriumClient getClientFromNode(INode node) {
        return nodeClients.get(node);
    }
}