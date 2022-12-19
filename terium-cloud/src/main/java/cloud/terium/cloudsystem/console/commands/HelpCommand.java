package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;

import java.util.Arrays;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "See all commands with description and aliases.", new String[]{"?", "question"}, null);
    }

    @Override
    public void execute(String[] args) {
        TeriumCloud.getTerium().getCommandManager().getCommandList().forEach(command -> Logger.log(command.getCommand() + Arrays.toString(command.getAliases()) + " - " + command.getDescription(), LogType.INFO));
    }
}