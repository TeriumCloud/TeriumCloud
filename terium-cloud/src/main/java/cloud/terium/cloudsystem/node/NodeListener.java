package cloud.terium.cloudsystem.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.event.events.node.NodeLoggedInEvent;
import cloud.terium.cloudsystem.event.events.node.NodeShutdownEvent;
import cloud.terium.cloudsystem.event.events.node.NodeShutdownedEvent;
import cloud.terium.cloudsystem.event.events.node.NodeUpdateEvent;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.node.INode;

public class NodeListener implements Listener {

    @Subscribe
    public void handleNodeLoggedIn(NodeLoggedInEvent event) {
        Logger.log("Node '" + event.getNode() + "' logged in with ip " + TeriumCloud.getTerium().getNodeProvider().getNodeByName(event.getNode()).orElseGet(null).getAddress().toString().replace("/", "") + ".", LogType.INFO);
        TeriumCloud.getTerium().getNodeProvider().getNodeByName(event.getNode()).ifPresent(INode::connect);
    }

    @Subscribe
    public void handleNodeShutdowned(NodeShutdownedEvent event) {
        Logger.log("Node '" + event.getNode().getName() + "' logged out with ip " + event.getNode().getAddress().toString().replace("/", "") + ".", LogType.INFO);
        event.getNode().stop();
    }

    @Subscribe
    public void handleNodeShutdown(NodeShutdownEvent event) {
        if (event.getNode().equals(TeriumCloud.getTerium().getThisNode().getName()))
            TeriumCloud.getTerium().shutdownCloud();
        else TeriumCloud.getTerium().getNodeProvider().getNodeByName(event.getNode()).ifPresent(INode::stop);
    }

    @Subscribe
    public void handleNodeUpdate(NodeUpdateEvent event) {
        TeriumCloud.getTerium().getNodeProvider().getNodeByName(event.getNode()).ifPresent(node -> {
            node.setMaxMemory(event.getMaxMemory());
            node.setUsedMemory(event.getUsedMemory());
        });
    }
}