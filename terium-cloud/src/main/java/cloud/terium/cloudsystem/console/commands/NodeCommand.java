package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.node.INode;

import java.util.Arrays;
import java.util.List;

public class NodeCommand extends Command {

    public NodeCommand() {
        super("node", "Interact with all registered nodes or add/remove a node.", "nodes");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "execute" -> {
                    if (args.length > 1) {
                        INode node = TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[1]);
                        try {
                            if(args[2].equalsIgnoreCase("disconnect")) node.disconnect();
                            if(args[2].equalsIgnoreCase("stop")) node.stop();
                        } catch (Exception exception) {
                            Logger.log("This node is currently not connected with this node.", LogType.ERROR);
                        }
                    } else Logger.log("node execute [name] disconnect | disconnect or stop a node", LogType.INFO);

                    return;
                }
                case "add" -> {
                    // TODO
                }
                case "remove" -> {
                    // TODO:
                }
                case "info" -> {
                    if (args.length > 1) {
                        INode node = TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[1]);
                        try {
                            String memoryColor;
                            if (node.getUsedMemory() > (node.getMaxMemory() / 1.3)) memoryColor = "§c";
                            else if (node.getUsedMemory() > (node.getMaxMemory() / 2)) memoryColor = "§6";
                            else memoryColor = "§a";
                            Logger.log("Name: " + node.getName() + "§7(" + (node.isConnected() ? "§aCONNECTED" : "§cNOT CONNECTED") + "§7)§f - Address: " + node.getAddress(), LogType.INFO);
                            Logger.log("Memory: " + memoryColor + node.getUsedMemory() + "§f/" + node.getMaxMemory(), LogType.INFO);
                        } catch (Exception exception) {
                            Logger.log("A node with that name isn't registered.", LogType.ERROR);
                        }
                    } else Logger.log("node info [name] | see all informations about a node", LogType.INFO);

                    return;
                }
                case "list" -> {
                    Logger.log(TeriumCloud.getTerium().getNodeProvider().getAllNodes().size() > 0 ? "All registered nodes:" : "There is no registered node.", LogType.INFO);
                    TeriumCloud.getTerium().getNodeProvider().getAllNodes().forEach(node -> Logger.log("Name: " + node.getName() + "§7(" + (node.isConnected() ? "§aCONNECTED" : "§cNOT CONNECTED") + "§7)§f - Address: " + node.getAddress(), LogType.INFO));
                    return;
                }
            }

            return;
        }

        Logger.log("node execute [name] disconnect | disconnect or stop a node", LogType.INFO);
        Logger.log("node add [name] | add a node", LogType.INFO);
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
                    return TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().map(INode::getName).toList();
                }
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("execute")) {
                    return Arrays.asList("disconnect", "stop");
                }
            }
        }

        return super.tabComplete(args);
    }
}