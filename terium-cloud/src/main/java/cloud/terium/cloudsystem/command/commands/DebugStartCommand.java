package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class DebugStartCommand extends Command {

    public DebugStartCommand(CommandManager commandManager) {
        super("debug-start");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            String[] command = args[0].split("-");
            int id = Integer.parseInt(command[1]);

            if (Terium.getTerium().getServiceManager().getMinecraftServiceCache().get(command[0] + (id > 9 ? "" + id : "-0" + id)) != null) {
                Logger.log("A service with this name already exist!", LogType.ERROR);
                return;
            }

            if (Terium.getTerium().getServiceGroupManager().getRegistedServerGroups().get(command[0]) != null) {
                Logger.log("Trying to start new service of group '" + command[0] + "'", LogType.INFO);
                new MinecraftService(Terium.getTerium().getServiceGroupManager().getRegistedServerGroups().get(command[0]), Integer.parseInt(command[1])).start();
                return;
            }
            Logger.log("This service group doesn't exist.", LogType.ERROR);
        } else {
            Logger.log("Syntax: debug-start <service-name>", LogType.INFO);
        }
    }
}
