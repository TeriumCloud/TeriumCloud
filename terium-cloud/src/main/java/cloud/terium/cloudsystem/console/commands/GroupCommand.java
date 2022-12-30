package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.pipe.TeriumServer;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import org.jline.utils.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class GroupCommand extends Command {

    public GroupCommand() {
        super("group", "Manage service groups.", "groups");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("lobby") || args[1].equalsIgnoreCase("server")) {
                        if (args.length == 6) {
                            switch (args[1]) {
                                case "lobby" -> TeriumCloud.getTerium().getServiceGroupProvider().registerServiceGroup(TeriumCloud.getTerium().getServiceGroupFactory().createLobbyGroup(args[2], "Default lobby group", TeriumCloud.getTerium().getThisNode(), List.of(), List.of(TeriumCloud.getTerium().getTemplateFactory().createTemplate(args[2])), args[3], Boolean.parseBoolean(args[4]), 20, Integer.parseInt(args[5]), 1, 1));
                                case "server" -> TeriumCloud.getTerium().getServiceGroupProvider().registerServiceGroup(TeriumCloud.getTerium().getServiceGroupFactory().createServerGroup(args[2], "Default server group", TeriumCloud.getTerium().getThisNode(), List.of(), List.of(TeriumCloud.getTerium().getTemplateFactory().createTemplate(args[2])), args[3], Boolean.parseBoolean(args[4]), 20, Integer.parseInt(args[5]), 1, 1));
                            }
                        } else Logger.log("group create lobby/server [name] [version] [static] [memory]", LogType.INFO);
                    } else {
                        if (args.length == 7) {
                            TeriumCloud.getTerium().getServiceGroupProvider().registerServiceGroup(TeriumCloud.getTerium().getServiceGroupFactory().createProxyGroup(args[2], "Default lobby group", TeriumCloud.getTerium().getThisNode(), List.of(), List.of(TeriumCloud.getTerium().getTemplateFactory().createTemplate(args[2])), args[3], Boolean.parseBoolean(args[4]), Integer.parseInt(args[6]), 20, Integer.parseInt(args[5]), 1, 1));
                        } else Logger.log("group create proxy [name] [version] [static] [memory] [port]", LogType.INFO);
                    }
                }
                return;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (args.length > 1) {
                    TeriumCloud.getTerium().getServiceGroupFactory().deleteServiceGroup(TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]));
                    TeriumCloud.getTerium().getTemplateFactory().deleteTemplate(args[1]);
                    Logger.log("Successfully deleted service group '" + args[1] + "'.", LogType.INFO);
                } else Logger.log("group delete [name]", LogType.INFO);
                return;
            }

            if (args[0].equalsIgnoreCase("info")) {
                if (args.length > 1) {
                    if(args.length == 3)
                        Logger.log(TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]).getInformationsFromJson(), LogType.INFO);
                    else Logger.log(TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]).getInformations(), LogType.INFO);
                } else Logger.log("group delete [name]", LogType.INFO);
                return;
            }

            if(args[0].equalsIgnoreCase("update")) {
                if(args.length > 1) {
                    if(args.length == 4) {
                        ICloudServiceGroup serviceGroup = TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]);
                        try {
                            switch (args[2]) {
                                case "maintenance" -> {
                                    if(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))
                                        serviceGroup.setMaintenance(Boolean.parseBoolean(args[3]));
                                }
                                case "static" -> {
                                    if(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))
                                        serviceGroup.setStatic(Boolean.parseBoolean(args[3]));
                                }
                                case "version" -> {
                                    if(Arrays.asList("paper-1.19.3", "paper-1.19.2", "paper-1.18.2", "paper-1.17.1", "paper-1.16.5", "paper-1.15.2", "paper-1.14.4", "paper-1.13.2", "paper-1.12.2", "windspogot-1.8.8", "minestom").contains(args[3]))
                                        serviceGroup.setVersion(args[3]);
                                }
                                case "memory" -> {
                                    try {
                                        serviceGroup.setMemory(Integer.parseInt(args[3]));
                                    } catch (Exception exception) {
                                        Logger.log("Please type a int as value.", LogType.ERROR);
                                    }
                                }
                                case "maxplayers" -> {
                                    try {
                                        serviceGroup.setMaxPlayer(Integer.parseInt(args[3]));
                                    } catch (Exception exception) {
                                        Logger.log("Please type a int as value.", LogType.ERROR);
                                    }
                                }
                                case "minservices" -> {
                                    try {
                                        serviceGroup.setMinServices(Integer.parseInt(args[3]));
                                    } catch (Exception exception) {
                                        Logger.log("Please type a int as value.", LogType.ERROR);
                                    }
                                }
                                case "maxservices" -> {
                                    try {
                                        serviceGroup.setMaxServices(Integer.parseInt(args[3]));
                                    } catch (Exception exception) {
                                        Logger.log("Please type a int as value.", LogType.ERROR);
                                    }
                                }
                            }
                            TeriumCloud.getTerium().getServiceGroupProvider().updateServiceGroup(serviceGroup);
                            Logger.log("Successfully updated service group '" + args[1] + "'.", LogType.INFO);
                        } catch (Exception exception) {
                            Logger.log("A service group with that name isn't registered.", LogType.ERROR);
                        }
                    }
                    return;
                }
                Logger.log("group update [name] [maintenance/static/version/memory/maxplayers/minservices/maxservices] [value] (--shutdown) | update a service group", LogType.INFO);
                return;
            }

            if(args[0].equalsIgnoreCase("add")) {
               if (args.length > 1) {
                   if (args.length == 4) {
                       ICloudServiceGroup serviceGroup = TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]);
                       try {
                           if (args[2].equalsIgnoreCase("fallback-node"))
                               serviceGroup.addFallbackNode(TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[3]));
                           else if (args[2].equalsIgnoreCase("template"))
                               serviceGroup.addTemplate(TeriumCloud.getTerium().getTemplateProvider().getTemplateByName(args[3]));
                           TeriumCloud.getTerium().getServiceGroupProvider().updateServiceGroup(serviceGroup);
                       } catch (Exception exception) {
                           exception.printStackTrace();
                           if(exception.getMessage() == null) {
                               if (args[2].equalsIgnoreCase("fallback-node"))
                                   Logger.log("A node with that name isn't registered.", LogType.ERROR);
                               else if (args[2].equalsIgnoreCase("template"))
                                   Logger.log("A template with that name isn't registered.", LogType.ERROR);
                           } else Logger.log("A service group with that name isn't registered.", LogType.ERROR);
                       }
                   }
               }
            }

            if(args[0].equalsIgnoreCase("remove")) {
                if (args.length > 1) {
                    if (args.length == 4) {
                        ICloudServiceGroup serviceGroup = TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]);
                        try {
                            if (args[2].equalsIgnoreCase("fallback-node"))
                                serviceGroup.removeFallbackNode(TeriumCloud.getTerium().getNodeProvider().getNodeByName(args[3]));
                            else if (args[2].equalsIgnoreCase("template"))
                                serviceGroup.removeTemplate(TeriumCloud.getTerium().getTemplateProvider().getTemplateByName(args[3]));
                             TeriumCloud.getTerium().getServiceGroupProvider().updateServiceGroup(serviceGroup);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            if(exception.getMessage() == null) {
                                if (args[2].equalsIgnoreCase("fallback-node"))
                                    Logger.log("A node with that name isn't added to the service group.", LogType.ERROR);
                                else if (args[2].equalsIgnoreCase("template"))
                                    Logger.log("A template with that name isn't added to the service group.", LogType.ERROR);
                            } else Logger.log("A service group with that name isn't registered.", LogType.ERROR);
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase("list")) {
                // TODO: Add online services count (after implement cloud services)
                if (TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().size() > 0)
                    TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().forEach(serviceGroup -> {
                        Logger.log("Name: " + serviceGroup.getGroupName() + "(" + serviceGroup.getServiceType().toString().toUpperCase() + ") - Online services: %NaN%" + " - Templates: " + serviceGroup.getTemplates().stream().map(ITemplate::getName).toList(), LogType.INFO);
                    });
                else Logger.log("There are no loaded service groups.", LogType.ERROR);
            }

            return;
        }

        Logger.log("group create lobby/server [name] [version] [static] [memory] | create a lobby/server service group", LogType.INFO);
        Logger.log("group create proxy [name] [version] [static] [memory] [port] | create a proxy service group", LogType.INFO);
        Logger.log("group delete [name] | delete a service group", LogType.INFO);
        Logger.log("group info [name] (--json) | see all informations about a service group", LogType.INFO);
        Logger.log("group update [name] [maintenance/version/static/memory/maxplayers/minservices/maxservices] [value] (--shutdown) | update a service group", LogType.INFO);
        Logger.log("group add/remove [name] [fallback-node/template] [value] | add or remove fallback node or template to a service group", LogType.INFO);
        Logger.log("group list | a list of all loaded service groups with informations", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        switch (args.length) {
            case 1 -> {
                return Arrays.asList("create", "delete", "add", "remove", "info", "update", "list");
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("update") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))
                    return TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().stream().map(ICloudServiceGroup::getGroupName).toList();
                if(args[0].equalsIgnoreCase("create"))
                    return Arrays.asList("lobby", "server", "proxy");
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("update"))
                    return Arrays.asList("maintenance", "version", "static", "memory", "maxplayers", "minservices", "maxservices");
                if(args[0].equalsIgnoreCase("info"))
                    return List.of("--json");
                if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))
                    return Arrays.asList("fallback-node", "template");
            }
            case 4 -> {
                if(args[1].equalsIgnoreCase("lobby") || args[1].equalsIgnoreCase("server"))
                    return Arrays.asList("paper-1.19.3", "paper-1.19.2", "paper-1.18.2", "paper-1.17.1", "paper-1.16.5", "paper-1.15.2", "paper-1.14.4", "paper-1.13.2", "paper-1.12.2", "windspogot-1.8.8", "minestom");
                if (args[1].equalsIgnoreCase("proxy"))
                    return Arrays.asList("velocity", "waterfall", "bungeecord");

                if(args[0].equalsIgnoreCase("update")) {
                    if (args[2].equalsIgnoreCase("maintenance") || args[2].equalsIgnoreCase("static"))
                        return Arrays.asList("true", "false");
                    else if (args[2].equalsIgnoreCase("version"))
                        return Arrays.asList("paper-1.19.3", "paper-1.19.2", "paper-1.18.2", "paper-1.17.1", "paper-1.16.5", "paper-1.15.2", "paper-1.14.4", "paper-1.13.2", "paper-1.12.2", "windspigot-1.8.8", "minestom");
                }

                if(args[0].equalsIgnoreCase("add")) {
                    if(args[2].equalsIgnoreCase("fallback-node"))
                        return TeriumCloud.getTerium().getNodeProvider().getAllNodes().stream().filter(node -> node != TeriumCloud.getTerium().getThisNode()).filter(node -> !TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]).getGroupFallbackNode().contains(node)).map(INode::getName).toList();
                    if(args[2].equalsIgnoreCase("template"))
                        return TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().stream().filter(template -> !TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]).getTemplates().contains(template)).map(ITemplate::getName).toList();
                }

                if(args[0].equalsIgnoreCase("remove")) {
                    if(args[2].equalsIgnoreCase("fallback-node"))
                        return TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]).getGroupFallbackNode().stream().map(INode::getName).toList();
                    if(args[2].equalsIgnoreCase("template"))
                        return TeriumCloud.getTerium().getServiceGroupProvider().getServiceGroupByName(args[1]).getTemplates().stream().map(ITemplate::getName).toList();
                }
            }
            case 5 -> {
                if (args[0].equalsIgnoreCase("update")) {
                    return List.of("--shutdown");
                }
            }
        }
        return super.tabComplete(args);
    }
}