package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;

import java.util.Arrays;
import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand(List<String[]> arguments) {
        super("help", "help", "See all commands with description, aliases and a small help", new String[]{"?", "hu/so"}, arguments);
    }

    @Override
    public void execute(String[] args) {
        Logger.log("name - aliases - help - description", LogType.INFO);
        Terium.getTerium().getCommandManager().getCommandList().forEach(command -> {
            Logger.log(command.getCommand() + " - " + command.getAliases().toString() + " - " + command.getHelp() + " - " + command.getDescription(), LogType.INFO);
        });
        Terium.getTerium().getCommandManager().registerCommand(new HelpCommand(Arrays.asList(new String[]{"%service%"}, new String[]{"-test1", "-test7"})));
    }
}
