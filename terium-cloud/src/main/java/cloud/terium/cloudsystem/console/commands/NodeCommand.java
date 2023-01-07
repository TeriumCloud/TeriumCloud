package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.node.INode;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NodeCommand extends Command {

    public NodeCommand() {
        super("node", "Interact with all registered nodes or add/remove a node.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "execute" -> {
                    if (args.length > 1) {
                        Optional<INode> cloudNode = TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[1]);
                        cloudNode.ifPresentOrElse(node -> {
                            if (args[2].equalsIgnoreCase("disconnect")) node.disconnect();
                            if (args[2].equalsIgnoreCase("stop")) node.stop();
                        }, () -> Logger.log("This node is currently not connected with this node.", LogType.ERROR));
                    } else Logger.log("node execute [name] disconnect§7|§fstop", LogType.INFO);

                    return;
                }
                case "add" -> {
                    if (args.length >= 5) {
                        try {
                            TeriumCloud.getTerium().getNodeFactory().createNode(args[1], args[4], new InetSocketAddress(args[2], Integer.parseInt(args[3])));
                            Logger.log("Successfully added node '" + args[1] + "'.", LogType.INFO);
                        } catch (Exception exception) {
                            if (args.length == 6 && args[5].equalsIgnoreCase("--print")) exception.printStackTrace();
                            Logger.log("Error while creating new node.", LogType.ERROR);
                        }
                    } else Logger.log("node add [name] [ip] [port] [key] (command)", LogType.INFO);

                    return;
                }
                case "remove" -> {
                    if (args.length > 1) {
                        Optional<INode> cloudNode = TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[1]);
                        cloudNode.ifPresentOrElse(node -> {
                            if (args[1].equalsIgnoreCase("--stop")) node.stop();
                            TeriumCloud.getTerium().getNodeFactory().deleteNode(node);
                        }, () -> Logger.log("A node with that name isn't registered.", LogType.ERROR));
                    } else Logger.log("node remove [name] (command)", LogType.INFO);

                    return;
                }
                case "info" -> {
                    if (args.length > 1) {
                        Optional<INode> cloudNode = TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[1]);
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
                    Logger.log(TeriumCloud.getTerium().getNodeProvider().getAllNodes().size() > 0 ? "All registered nodes:" : "There is no registered node.", LogType.INFO);
                    TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().filter(node -> node != TeriumCloud.getTerium().getThisNode()).forEach(node -> Logger.log("Name: " + node.getName() + "§7(" + (node.isConnected() ? "§aCONNECTED" : "§cNOT CONNECTED") + "§7)§f - Address: " + node.getAddress().getHostName() + ":" + node.getAddress().getPort(), LogType.INFO));
                    return;
                }
            }

            return;
        }

        Logger.log("node execute [name] disconnect§7|§fstop | disconnect or stop a node", LogType.INFO);
        Logger.log("node add [name] [ip] [port] [key] (command) | add a node", LogType.INFO);
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
                    return TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().filter(node -> node != TeriumCloud.getTerium().getThisNode()).map(INode::getName).toList();
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