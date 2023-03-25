package cloud.terium.cloudsystem.cluster.node;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.common.event.events.node.NodeLoggedInEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownedEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeUpdateEvent;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.node.INode;

public class NodeListener implements Listener {

    @Subscribe
    public void handleNodeLoggedIn(NodeLoggedInEvent event) {
        Logger.log("Node '" + event.getNode() + "' sucessfully logged in.", LogType.INFO);
    }

    @Subscribe
    public void handleNodeShutdowned(NodeShutdownedEvent event) {
        Logger.log("Node '" + event.getNode().getName() + "' sucessfully logged out.", LogType.INFO);
        event.getNode().stop();
    }

    @Subscribe
    public void handleNodeShutdown(NodeShutdownEvent event) {
        if (event.getNode().equals(ClusterStartup.getCluster().getThisNode().getName()))
            ClusterStartup.getCluster().shutdownCloud();
        else ClusterStartup.getCluster().getNodeProvider().getNodeByName(event.getNode()).ifPresent(INode::stop);
    }

    @Subscribe
    public void handleNodeUpdate(NodeUpdateEvent event) {
        ClusterStartup.getCluster().getNodeProvider().getNodeByName(event.getNode()).ifPresent(node -> {
            node.setMaxMemory(event.getMaxMemory());
            node.setUsedMemory(event.getUsedMemory());
        });
    }
}