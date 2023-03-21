package cloud.terium.cloudsystem.node.node;

import cloud.terium.cloudsystem.node.NodeStartup;
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
        NodeStartup.getNode().getConfigManager().getJson().get("nodes").getAsJsonObject().add(name, jsonObject);
        NodeStartup.getNode().getConfigManager().save();
        NodeStartup.getNode().getNodeProvider().registerNode(new Node(name, key, address));
    }

    @Override
    public void deleteNode(INode node) {
        NodeStartup.getNode().getNodeProvider().getAllNodes().remove(node);
        NodeStartup.getNode().getConfigManager().getJson().get("nodes").getAsJsonObject().remove(node.getName());
        NodeStartup.getNode().getConfigManager().save();
    }
}