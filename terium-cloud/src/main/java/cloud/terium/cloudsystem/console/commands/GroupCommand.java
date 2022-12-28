package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.Arrays;
import java.util.List;

public class GroupCommand extends Command {

    public GroupCommand() {
        super("group", "Manage service groups.", "groups");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            if(args[0].equalsIgnoreCase("delete")) {
                if (args.length > 1) {
                    TeriumCloud.getTerium().getServiceGroupFactory().deleteServiceGroup(TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]));
                    Logger.log("Successfully deleted service group '" + args[1] + "'.", LogType.INFO);
                } else Logger.log("group delete [name]", LogType.INFO);
                return;
            }

            if(args[0].equalsIgnoreCase("list")) {
                // TODO: Add online services count (after implement cloud services)
                if (TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().size() > 0)
                    TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().forEach(serviceGroup -> {
                        Logger.log("Name: " + serviceGroup.getServiceGroupName() + " - Online services: %NaN%" + " - Templates: " + serviceGroup.getTemplates().stream().map(ITemplate::getName).toList(), LogType.INFO);
                    });
                else Logger.log("There are no loaded service groups.", LogType.ERROR);
            }

            return;
        }

        Logger.log("group create [name] [type] [port] [key] | create a service group", LogType.INFO);
        Logger.log("group delete [name] | delete a service group", LogType.INFO);
        Logger.log("group info [name] | see all informations about a service group", LogType.INFO);
        Logger.log("group update [name] [maintenance/static/memory/maxplayers/minservices/maxservices] [value] | update a service group", LogType.INFO);
        Logger.log("group list | a list of all loaded service groups with informations", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        switch (args.length) {
            case 1 -> {
                return Arrays.asList("create", "delete", "info", "update", "list");
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("update") || args[0].equalsIgnoreCase("delete"))
                    return TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().stream().map(ICloudServiceGroup::getServiceGroupName).toList();
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("update"))
                    return Arrays.asList("maintenance", "static", "memory", "maxplayers", "minservices", "maxservices");
            }
            case 4 -> {
                if(args[2].equalsIgnoreCase("maintenance") || args[2].equalsIgnoreCase("static"))
                    return Arrays.asList("true", "false");
                else if(args[2].equalsIgnoreCase("memory"))
                    return Arrays.asList("128", "512", "1024", "2048");
                else if(args[2].equalsIgnoreCase("maxplayers"))
                    return Arrays.asList("1", "5", "10", "20");
                else if(args[2].equalsIgnoreCase("minservices") || args[2].equalsIgnoreCase("maxservices"))
                    return Arrays.asList("1", "2", "3", "4", "5");
            }
        }
        return super.tabComplete(args);
    }
}