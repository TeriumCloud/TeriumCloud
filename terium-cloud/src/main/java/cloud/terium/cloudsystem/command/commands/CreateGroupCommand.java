package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;

public class CreateGroupCommand extends Command {

    /*
     * TODO: Renew create-group Command
     */

    public CreateGroupCommand(CommandManager commandManager) {
        super("create-group");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        // TODO: Recode
    }
}
