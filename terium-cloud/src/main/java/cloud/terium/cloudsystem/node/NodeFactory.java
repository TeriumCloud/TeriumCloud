package cloud.terium.cloudsystem.node;

import cloud.terium.cloudsystem.TeriumCloud;
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
        TeriumCloud.getTerium().getConfigManager().getJson().get("nodes").getAsJsonObject().add(name, jsonObject);
        TeriumCloud.getTerium().getConfigManager().save();
        TeriumCloud.getTerium().getNodeProvider().registerNode(new Node(name, key, address));
    }

    @Override
    public void deleteNode(INode node) {
        TeriumCloud.getTerium().getNodeProvider().getAllNodes().remove(node);
        TeriumCloud.getTerium().getConfigManager().getJson().get("nodes").getAsJsonObject().remove(node.getName());
        TeriumCloud.getTerium().getConfigManager().save();
    }
}