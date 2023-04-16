package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.template.ITemplate;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class InfoCommand extends Command {

    public InfoCommand() {
        super("info", "See informations about the current cloud-system instance.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "cloud" -> {
                    Logger.log("Version: §b" + TeriumCloud.getTerium().getCloudUtils().getVersion(), LogType.INFO);
                    Logger.log("Memory Usage: " + (ClusterStartup.getCluster().getThisNode().getUsedMemory() > (ClusterStartup.getCluster().getThisNode().getMaxMemory() / 1.7) ? "§6" : "§a") + ClusterStartup.getCluster().getThisNode().getUsedMemory() + "§f of §b" + ClusterStartup.getCluster().getThisNode().getMaxMemory(), LogType.INFO);
                    Logger.log("Connection: §c" + ((InetSocketAddress) ClusterStartup.getCluster().getNetworking().getChannel().localAddress()).getAddress().getHostAddress() + ":" + ((InetSocketAddress) ClusterStartup.getCluster().getNetworking().getChannel().localAddress()).getPort(), LogType.INFO);
                    return;
                }
                case "servers" -> {
                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(cloudService -> {
                        Logger.log("§7● §b" + cloudService.getServiceName() + "§f:", LogType.INFO);
                        Logger.log("  §7● §fID: #" + cloudService.getServiceId() + " §7| §fState: " + cloudService.getServiceState(), LogType.INFO);
                        Logger.log("  §7● §fType: " + cloudService.getServiceType() + " §7| §fTemplates: " + cloudService.getTemplates().stream().map(ITemplate::getName).toList(), LogType.INFO);
                        Logger.log("  §7● §fHost: " + cloudService.getServiceGroup().getGroupNode().getAddress().getAddress().getHostAddress() + ":" + cloudService.getPort() + " §7| §fPlayers: " + cloudService.getOnlinePlayers() + "/" + cloudService.getMaxPlayers(), LogType.INFO);
                        Logger.log("  §7● §fMemory: " + cloudService.getUsedMemory() + "/" + cloudService.getMaxMemory(), LogType.INFO);
                        if (cloudService.getPropertyMap() == null || cloudService.getPropertyMap().keySet().isEmpty()) {
                            Logger.log("  §7● §fProperties: §cNone", LogType.INFO);
                        } else {
                            Logger.log("  §7● §fProperties: §a" + cloudService.getPropertyMap().keySet().size(), LogType.INFO);
                            cloudService.getPropertyMap().keySet().forEach(s -> {
                                Logger.log("     §7● §f" + s + ": §6" + cloudService.getPropertyMap().get(s), LogType.INFO);
                            });
                        }
                    });

                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName("Lobby-01").ifPresent(cloudService -> cloudService.addProperty("test", true));
                    return;
                }
            }

            return;
        }

        Logger.log("info cloud | see informations about the current cloud-system instance", LogType.INFO);
        Logger.log("info servers | see informations about all connected services", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1)
            return Arrays.asList("cloud", "servers");

        return super.tabComplete(args);
    }
}