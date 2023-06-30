package cloud.terium.cloudsystem.cluster.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.event.events.node.NodeLoggedInEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownedEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeUpdateEvent;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

public class NodeListener implements Listener {

    @Subscribe
    public void handleNodeLoggedIn(NodeLoggedInEvent event) {
        Logger.log("Node '§b" + event.getNode() + "§f' sucessfully logged in.", LogType.INFO);
    }

    @Subscribe
    public void handleNodeShutdowned(NodeShutdownedEvent event) {
        Logger.log("Node '§b" + event.getNode() + "§f' sucessfully logged out.", LogType.INFO);
    }

    @Subscribe
    public void handleNodeUpdate(NodeUpdateEvent event) {
        ClusterStartup.getCluster().getNodeProvider().getNodeByName(event.getNode()).ifPresent(node -> {
            node.setMaxMemory(event.getMaxMemory());
            node.setUsedMemory(event.getUsedMemory());
        });
    }
}