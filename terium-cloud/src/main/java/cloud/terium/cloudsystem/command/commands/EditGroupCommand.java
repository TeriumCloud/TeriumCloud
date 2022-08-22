package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.service.ServiceType;
import cloud.terium.cloudsystem.service.group.DefaultServiceGroup;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import org.jline.utils.Log;

public class EditGroupCommand  extends Command {

    public EditGroupCommand(CommandManager commandManager) {
        super("edit-group");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        switch (args.length) {
            case 1 -> {

            }
            case 2 -> {

            }
            default -> {
                Logger.log("Syntax: edit <group_name> <maintenance/maxPlayers/maxMemory/minimumOnlineServices/maximumOnlineServices> <value>");
            }
        }
    }
}
