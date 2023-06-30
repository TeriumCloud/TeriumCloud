package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;

import java.util.Arrays;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "See all commands with description and aliases.", "?");
    }

    @Override
    public void execute(String[] args) {
        ClusterStartup.getCluster().getCommandManager().getCommandList().forEach(command -> Logger.log(command.getCommand() + (command.getAliases().length >= 1 ? Arrays.asList(command.getAliases()) : "") + " - " + command.getDescription(), LogType.INFO));
    }
}