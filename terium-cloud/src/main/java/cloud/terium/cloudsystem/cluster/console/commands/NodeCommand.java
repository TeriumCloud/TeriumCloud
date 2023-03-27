package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.node.Node;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.node.INode;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class NodeCommand extends Command {

    public NodeCommand() {
        super("node", "Interact with all registered nodes or add/remove a node.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "add" -> {
                    if (args.length >= 4) {
                        try {
                            ClusterStartup.getCluster().getNodeProvider().getNodeByName(args[1]).ifPresentOrElse(node -> Logger.log("A node with that name is already registered!", LogType.ERROR), () -> {
                                JsonObject newNode = new JsonObject();
                                newNode.addProperty("name", args[1]);
                                newNode.addProperty("ip", args[2]);
                                newNode.addProperty("port", args[3]);
                                ClusterStartup.getCluster().getCloudConfig().nodes().add(args[1], newNode);
                                ClusterStartup.getCluster().getConfigManager().save();
                                ClusterStartup.getCluster().getNodeProvider().registerNode(new Node(args[1], "", new InetSocketAddress(args[2], Integer.parseInt(args[3]))));

                                Logger.log("Successfully added node '" + args[1] + "'.", LogType.INFO);
                            });
                        } catch (Exception exception) {
                            if (args.length == 6 && args[5].equalsIgnoreCase("--print")) exception.printStackTrace();
                            Logger.log("Error while creating new node.", LogType.ERROR);
                        }
                    } else Logger.log("node add [name] [ip] [port] (command)", LogType.INFO);

                    return;
                }
                case "remove" -> {
                    if (args.length > 1) {
                        ClusterStartup.getCluster().getNodeProvider().getNodeByName(args[1]).ifPresentOrElse(node -> {
                            ClusterStartup.getCluster().getCloudConfig().nodes().remove(args[1]);
                            ClusterStartup.getCluster().getConfigManager().save();
                            ClusterStartup.getCluster().getServiceGroupProvider().getAllServiceGroups().stream().filter(serviceGroup -> serviceGroup.getGroupNode().getName().equals(args[1])).forEach(serviceGroup -> {
                                serviceGroup.setGroupNode(ClusterStartup.getCluster().getThisNode());
                            });
                        }, () -> Logger.log("A node with that name isn't registered.", LogType.ERROR));
                    } else Logger.log("node remove [name]", LogType.INFO);

                    return;
                }
                case "info" -> {
                    if (args.length > 1) {
                        Optional<INode> cloudNode = ClusterStartup.getCluster().getNodeProvider().getNodeByName(args[1]);
                        cloudNode.ifPresentOrElse(node -> {
                            String memoryColor;
                            if (node.getUsedMemory() > (node.getMaxMemory() / 1.3)) memoryColor = "§c";
                            else if (node.getUsedMemory() > (node.getMaxMemory() / 2)) memoryColor = "§6";
                            else memoryColor = "§a";
                            Logger.log("Name: " + node.getName() + "§7(" + (node.isConnected() ? "§aCONNECTED" : "§cNOT CONNECTED") + "§7)§f - Address: " + node.getAddress().getHostName() + ":" + node.getAddress().getPort(), LogType.INFO);
                            Logger.log("Memory: " + memoryColor + node.getUsedMemory() + "§f/" + node.getMaxMemory(), LogType.INFO);
                        }, () -> Logger.log("A node with that name isn't registered.", LogType.ERROR));
                    } else Logger.log("node info [name]", LogType.INFO);

                    return;
                }
                case "list" -> {
                    Logger.log(ClusterStartup.getCluster().getNodeProvider().getAllNodes().size() > 0 ? "All registered nodes:" : "There is no registered node.", LogType.INFO);
                    ClusterStartup.getCluster().getNodeProvider().getAllNodes().stream().filter(node -> node != ClusterStartup.getCluster().getThisNode()).forEach(node -> Logger.log("Name: " + node.getName() + "§7(" + (node.isConnected() ? "§aCONNECTED" : "§cNOT CONNECTED") + "§7)§f - Address: " + node.getAddress().getHostName() + ":" + node.getAddress().getPort(), LogType.INFO));
                    return;
                }
            }

            return;
        }

        Logger.log("node add [name] [ip] [port] (command) | add a node", LogType.INFO);
        Logger.log("node remove [name] | remove a node", LogType.INFO);
        Logger.log("node info [name] | see all informations about a node", LogType.INFO);
        Logger.log("nodes list | a list of all loaded nodes with informations", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        switch (args.length) {
            case 1 -> {
                return Arrays.asList("execute", "add", "remove", "info", "list");
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("execute") || args[0].equalsIgnoreCase("info")) {
                    return ClusterStartup.getCluster().getNodeProvider().getAllNodes().stream().filter(node -> node != ClusterStartup.getCluster().getThisNode()).map(INode::getName).toList();
                }
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("execute")) {
                    return Arrays.asList("disconnect", "stop");
                }
            }
            case 6 -> {
                return List.of("--print");
            }
        }

        return super.tabComplete(args);
    }
}