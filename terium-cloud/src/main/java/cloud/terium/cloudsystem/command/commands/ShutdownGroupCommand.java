package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.service.ICloudService;

public class ShutdownGroupCommand extends Command {

    public ShutdownGroupCommand(CommandManager commandManager) {
        super("shutdown-group");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]) != null) {
                Logger.log("Terium is trying to stop all services from group '" + args[0] + "'.", LogType.INFO);
                Terium.getTerium().getServiceManager().getCloudServicesByGroupName(args[0]).forEach(ICloudService::shutdown);
            } else {
                Logger.log("Terium could't find a service with name '" + args[0] + "'.", LogType.ERROR);
            }
        }else {
            Logger.log("Syntax: shutdown-group <service_group>", LogType.INFO);
        }
    }
}