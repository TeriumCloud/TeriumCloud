package cloud.terium.cloudsystem.cluster.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Getter
public class NodeProvider implements INodeProvider {

    private final HashMap<String, INode> nodes;
    private final HashMap<INode, ChannelHandlerContext> nodeClients;

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
        registerNode(ClusterStartup.getCluster().getThisNode());

        JsonObject jsonObject = ClusterStartup.getCluster().getCloudConfig().nodes();
        ClusterStartup.getCluster().getCloudConfig().nodes().entrySet().forEach(jsonElement -> registerNode(new Node(jsonElement.getValue().getAsJsonObject().get("name").getAsString(),
                new InetSocketAddress(jsonElement.getValue().getAsJsonObject().get("ip").getAsString(), jsonElement.getValue().getAsJsonObject().get("port").getAsInt()))));
    }

    @Override
    public List<INode> getAllNodes() {
        return nodes.values().stream().toList();
    }

    @Override
    public Optional<INode> getNodeByName(String name) {
        return Optional.ofNullable(nodes.get(name));
    }

    public void addClientToNode(INode node, ChannelHandlerContext channel) {
        nodeClients.put(node, channel);
    }

    public ChannelHandlerContext getClientFromNode(INode node) {
        return nodeClients.get(node);
    }
}