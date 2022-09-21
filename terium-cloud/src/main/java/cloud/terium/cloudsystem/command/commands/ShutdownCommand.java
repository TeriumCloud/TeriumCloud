package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class ShutdownCommand extends Command {

    public ShutdownCommand(CommandManager commandManager) {
        super("shutdown");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]) != null) {
                Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]).shutdown();
            } else {
                Logger.log("Terium could't find a service with name '" + args[0] + "'.", LogType.ERROR);
            }
        } else {
            Logger.log("Syntax: shutdown <service_name>", LogType.INFO);
        }
    }
}