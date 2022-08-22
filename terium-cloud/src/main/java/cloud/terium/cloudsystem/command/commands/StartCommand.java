package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class StartCommand extends Command {

    public StartCommand(CommandManager commandManager) {
        super("start");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (Terium.getTerium().getServiceGroupManager().getRegistedServerGroups().get(args[0]) != null) {
                Logger.log("Trying to start new service of group '" + args[0] + "'", LogType.INFO);
                new MinecraftService(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0])).start();
                return;
            }
            Logger.log("This service group doesn't exist.", LogType.ERROR);
        } else if (args.length == 2) {
            if (Terium.getTerium().getServiceGroupManager().getRegistedServerGroups().get(args[0]) != null) {
                Logger.log("Trying to start " + args[1] + " new services of group '" + args[0] + "'", LogType.INFO);
                for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                    new MinecraftService(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0])).start();
                }
                return;
            }
            Logger.log("This service group doesn't exist.", LogType.ERROR);
        } else {
            Logger.log("Syntax: start <group_name> (number)", LogType.INFO);
        }
    }
}
