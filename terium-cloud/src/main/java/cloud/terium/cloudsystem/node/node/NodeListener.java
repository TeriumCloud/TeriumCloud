package cloud.terium.cloudsystem.node.node;

import cloud.terium.cloudsystem.common.event.events.node.NodeLoggedInEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeShutdownedEvent;
import cloud.terium.cloudsystem.common.event.events.node.NodeUpdateEvent;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.node.INode;

public class NodeListener implements Listener {

    @Subscribe
    public void handleNodeLoggedIn(NodeLoggedInEvent event) {
        Logger.log("Node '" + event.getNode() + "' logged in with ip " + NodeStartup.getNode().getNodeProvider().getNodeByName(event.getNode()).orElseGet(null).getAddress().toString().replace("/", "") + ".", LogType.INFO);
        NodeStartup.getNode().getNodeProvider().getNodeByName(event.getNode()).ifPresent(INode::connect);
    }

    @Subscribe
    public void handleNodeShutdowned(NodeShutdownedEvent event) {
        Logger.log("Node '" + event.getNode().getName() + "' logged out with ip " + event.getNode().getAddress().toString().replace("/", "") + ".", LogType.INFO);
        event.getNode().stop();
    }

    @Subscribe
    public void handleNodeShutdown(NodeShutdownEvent event) {
        if (event.getNode().equals(NodeStartup.getNode().getThisNode().getName()))
            NodeStartup.getNode().shutdownCloud();
        else NodeStartup.getNode().getNodeProvider().getNodeByName(event.getNode()).ifPresent(INode::stop);
    }

    @Subscribe
    public void handleNodeUpdate(NodeUpdateEvent event) {
        NodeStartup.getNode().getNodeProvider().getNodeByName(event.getNode()).ifPresent(node -> {
            node.setMaxMemory(event.getMaxMemory());
            node.setUsedMemory(event.getUsedMemory());
        });
    }
}