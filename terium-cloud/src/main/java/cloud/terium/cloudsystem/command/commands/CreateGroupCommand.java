package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.service.ServiceType;
import cloud.terium.cloudsystem.service.group.DefaultServiceGroup;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class CreateGroupCommand extends Command {

    public CreateGroupCommand(CommandManager commandManager) {
        super("create-group");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 5) {
            new DefaultServiceGroup(args[0], args[1], ServiceType.valueOf(args[2]), false, Integer.parseInt(args[3]), Integer.parseInt(args[4]), 1, 1).createGroup();
            Logger.log("Creating new server-group named '" + args[0] + "'.", LogType.INFO);
        } else if (args.length == 6) {
            new DefaultServiceGroup(args[0], args[1], ServiceType.valueOf(args[2]), false, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), 1, 1).createGroup();
            Logger.log("Creating new server-group named '" + args[0] + "' with port " + args[3] + ".", LogType.INFO);
        } else {
            Logger.log("Syntax: create-group <name> <group_title> <servicetype> <is proxy: port> <max_players> <memory>", LogType.INFO);
        }
    }
}
