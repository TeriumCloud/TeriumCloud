package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public class GroupCommand extends Command {

    public GroupCommand(CommandManager commandManager) {
        super("group");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            if (Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]) != null) {
                ICloudServiceGroup iCloudServiceGroup = Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]);

                switch (args[1]) {
                    case "info" -> {
                        Logger.log("Information of service-group '" + iCloudServiceGroup.getServiceGroupName() + "':", LogType.INFO);
                        Logger.log("{   ");
                        Logger.log("    Name: " + iCloudServiceGroup.getServiceGroupName());
                        Logger.log("    Title: " + iCloudServiceGroup.getGroupTitle());
                        Logger.log("    Type: " + iCloudServiceGroup.getServiceType());
                        Logger.log("    Node: " + iCloudServiceGroup.getServiceGroupNode());
                        Logger.log("    Maintenance: " + iCloudServiceGroup.isMaintenance());
                        Logger.log("    Version: " + iCloudServiceGroup.getVersion());
                        Logger.log("    Max memory: " + iCloudServiceGroup.getMemory());
                        Logger.log("    Max players: " + iCloudServiceGroup.getMaximumPlayers());
                        Logger.log("    Minimal services: " + iCloudServiceGroup.getMinimalServices());
                        Logger.log("    Maximal services: " + iCloudServiceGroup.getMaximalServices());
                        Logger.log("}   ");
                    }
                    case "shutdown" ->
                            Terium.getTerium().getServiceManager().getCloudServicesByGroupName(args[0]).forEach(ICloudService::shutdown);
                    case "start" -> Logger.log("Syntax: group <service-group> info|shutdown|start<amout>", LogType.INFO);
                }
            } else {
                Logger.log("Terium could't find a service-group with name '" + args[0] + "'.", LogType.ERROR);
            }
            return;
        }

        if (args.length == 3) {
            if (args[1].equals("start")) {
                if (Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]) != null) {
                    Logger.log("Trying to start " + args[2] + " new services of group '" + args[0] + "'", LogType.INFO);
                    for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                        new MinecraftService(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0])).start();
                    }
                    return;
                } else {
                    Logger.log("Terium could't find a service-group with name '" + args[0] + "'.", LogType.ERROR);
                }
            }
        }

        Logger.log("Syntax: group <service-group> info|shutdown|start<amout>", LogType.INFO);
    }
}
