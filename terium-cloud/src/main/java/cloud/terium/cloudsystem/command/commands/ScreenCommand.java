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
     * TODO: Implement that command in service <service-name> screen
     * TODO: Clear console after toggle screen and insert all cached logs.
     */

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if(Terium.getTerium().getCloudUtils().isInScreen() && !Terium.getTerium().getScreenManager().getCurrentScreen().equals(Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]))) return;

            if (Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]) != null) {
                Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]).toggleScreen();
            }
        } else {
            Logger.log("Syntax: screen <service>", LogType.INFO);
        }
    }
}