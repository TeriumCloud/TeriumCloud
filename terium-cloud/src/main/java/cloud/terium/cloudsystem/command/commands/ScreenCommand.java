package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;

public class ScreenCommand extends Command {

    public ScreenCommand(CommandManager commandManager) {
        super("screen");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {

    }
}
