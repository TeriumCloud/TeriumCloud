package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import org.jline.builtins.Completers;

import java.util.Arrays;
import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "See all commands with description and aliases.", "?");
    }

    @Override
    public void execute(String[] args) {
        TeriumCloud.getTerium().getCommandManager().getCommandList().forEach(command -> Logger.log(command.getCommand() + (command.getAliases().length >= 1 ? Arrays.asList(command.getAliases()) : "") + " - " + command.getDescription(), LogType.INFO));
    }
}