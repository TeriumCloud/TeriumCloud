package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class ScreenCommand extends Command {

    public ScreenCommand(CommandManager commandManager) {
        super("screen");
        commandManager.register(this);
    }

    /*
     * TODO: Make that you can't go in a other screen while you are in a screen
     * TODO: Make for the screen a custom "console".
     */

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]) != null) {
                Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]).toggleScreen();
            }
        } else {
            Logger.log("Syntax: screen <service>", LogType.INFO);
        }
    }
}