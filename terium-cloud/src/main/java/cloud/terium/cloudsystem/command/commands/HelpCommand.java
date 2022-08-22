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
        Logger.log("create-group | create a servergroup", LogType.INFO);
        Logger.log("start | start a service by servergroup", LogType.INFO);
        Logger.log("shutdown | stop a service", LogType.INFO);
        Logger.log("stop | stop the cloudsystem", LogType.INFO);
    }
}
