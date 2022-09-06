package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

public class ListModulesCommand extends Command {


    public ListModulesCommand(CommandManager commandManager) {
        super("list-modules");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        Logger.log("List of all loaded modules: ", LogType.INFO);
        Terium.getTerium().getModuleManager().getAllModules().forEach(module -> {
            Logger.log("Module: " + module.getName() + " by " + module.getAuthor() + " (v" + module.getVersion() + ")", LogType.INFO);
        });
    }
}
