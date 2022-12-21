package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.teriumapi.console.command.Command;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Clear the console.");
    }

    @Override
    public void execute(String[] args) {
        TeriumCloud.getTerium().getConsoleManager().clearScreen();
    }
}
