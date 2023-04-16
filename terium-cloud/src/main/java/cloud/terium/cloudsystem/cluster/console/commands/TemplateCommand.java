package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TemplateCommand extends Command {

    public TemplateCommand() {
        super("template", "A list of all tempaltes");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "create" -> {
                    if (args.length > 1) {
                        Logger.log("Trying to create template '§b" + args[1] + "§f'...", LogType.INFO);
                        ClusterStartup.getCluster().getTemplateFactory().createTemplate(args[1]);
                    } else Logger.log("template create [name] | create a new template", LogType.INFO);
                    return;
                }
                case "delete" -> {
                    if (args.length > 1) {
                        Logger.log("Trying to delete template '§b" + args[1] + "§f'...", LogType.INFO);
                        ClusterStartup.getCluster().getTemplateFactory().deleteTemplate(args[1]);
                        Logger.log("Successfully deleted template '§b" + args[1] + "§f'.", LogType.INFO);
                    } else Logger.log("template delete [name] | delete a template", LogType.INFO);
                    return;
                }
                case "info" -> {
                    if (args.length > 1) {
                        Optional<ITemplate> cloudTemplate = ClusterStartup.getCluster().getTemplateProvider().getTemplateByName(args[1]);
                        cloudTemplate.ifPresentOrElse(template -> {
                            Logger.log("Informations of template '§b" + template.getName() + "§f':", LogType.INFO);
                            Logger.log("Name: " + template.getName() + " - Path: " + template.getPath().toString(), LogType.INFO);
                        }, () -> Logger.log("A template with that name isn't registered.", LogType.ERROR));
                    } else Logger.log("template info [name] | see all informations about a template", LogType.INFO);

                    return;
                }
                case "list" -> {
                    Logger.log("List of all tempaltes:", LogType.INFO);
                    ClusterStartup.getCluster().getTemplateProvider().getAllTemplates().forEach(template -> Logger.log("Name: " + template.getName() + " - Path: " + template.getPath().toString(), LogType.INFO));
                    return;
                }
            }
        }

        Logger.log("template create [name] | create a new template", LogType.INFO);
        Logger.log("template delete [name] | delete a template", LogType.INFO);
        Logger.log("template info [name] | see all informations about a template", LogType.INFO);
        Logger.log("template list | a list of all loaded tempaltes", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "delete", "info", "list");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("delete"))) {
            return ClusterStartup.getCluster().getTemplateProvider().getAllTemplates().stream().map(ITemplate::getName).toList();
        }
        return super.tabComplete(args);
    }
}
