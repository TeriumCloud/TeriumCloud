package cloud.terium.cloudsystem.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

public class NodeProvider implements INodeProvider {

    private final HashMap<String, INode> nodes;

    public NodeProvider() {
        this.nodes = new HashMap<>();
        TeriumCloud.getTerium().getCloudConfig().nodes().forEach(jsonElement -> registerNode(new Node(jsonElement.getAsJsonObject().get("name").getAsString(), jsonElement.getAsJsonObject().get("key").getAsString(),
                new InetSocketAddress(jsonElement.getAsJsonObject().get("ip").getAsString(), jsonElement.getAsJsonObject().get("port").getAsInt()))));
    }

    public void registerNode(INode node) {
        this.nodes.put(node.getName(), node);
    }

    @Override
    public List<INode> getAllNodes() {
        return nodes.values().stream().toList();
    }

    @Override
    public INode getNodeByName(String name) {
        return this.nodes.get(name);
    }
}
