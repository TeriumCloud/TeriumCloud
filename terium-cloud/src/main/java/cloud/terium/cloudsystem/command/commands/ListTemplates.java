package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;

public class ListTemplates extends Command {

    public ListTemplates(CommandManager commandManager) {
        super("list-templates");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        Logger.log("List of all templates: ", LogType.INFO);
        System.out.println(" ");
        Terium.getTerium().getTemplateManager().getAllTemplates().forEach(template -> {
            Logger.log("Template: " + template.getName() + " / Path: " + template.getPath().toString(), LogType.INFO);
        });
    }
}
