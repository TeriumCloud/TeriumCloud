package cloud.terium.cloudsystem.cluster.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeFactory;
import com.google.gson.JsonObject;

import java.net.InetSocketAddress;

public class NodeFactory implements INodeFactory {

    @Override
    public void createNode(String name, String key, InetSocketAddress address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("key", key);
        jsonObject.addProperty("address", address.getHostName());
        jsonObject.addProperty("port", address.getPort());
        ClusterStartup.getCluster().getConfigManager().getJson().get("nodes").getAsJsonObject().add(name, jsonObject);
        ClusterStartup.getCluster().getConfigManager().save();
        ClusterStartup.getCluster().getNodeProvider().registerNode(new Node(name, key, address));
    }

    @Override
    public void deleteNode(INode node) {
        ClusterStartup.getCluster().getNodeProvider().getAllNodes().remove(node);
        ClusterStartup.getCluster().getConfigManager().getJson().get("nodes").getAsJsonObject().remove(node.getName());
        ClusterStartup.getCluster().getConfigManager().save();
    }
}