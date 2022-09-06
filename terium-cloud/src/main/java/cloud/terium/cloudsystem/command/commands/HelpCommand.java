package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class HelpCommand extends Command {

    public HelpCommand(CommandManager commandManager) {
        super("help");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        Logger.log("Help for Terium:", LogType.INFO);
        Logger.log("help | send this help message", LogType.INFO);
        Logger.log("create-group | create a service group", LogType.INFO);
        Logger.log("edit-group | Edit a service group", LogType.INFO);
        Logger.log("delete-group | delete a service group", LogType.INFO);
        Logger.log("start | start a service by service group", LogType.INFO);
        Logger.log("shutdown | stop a service", LogType.INFO);
        Logger.log("shutdown-group | stop a service group", LogType.INFO);
        Logger.log("list | list all online services", LogType.INFO);
        Logger.log("list-modules | list all loaded modules", LogType.INFO);
        Logger.log("reload | reload the cloud or a part of the cloud", LogType.INFO);
        Logger.log("stop | stop the cloudsystem", LogType.INFO);
    }
}
