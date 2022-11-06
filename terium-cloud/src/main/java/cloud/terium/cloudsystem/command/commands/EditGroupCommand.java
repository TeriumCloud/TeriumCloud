package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packets.PacketPlayOutGroupReload;
import cloud.terium.teriumapi.console.LogType;

public class EditGroupCommand extends Command {

    public EditGroupCommand(CommandManager commandManager) {
        super("edit-group");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        switch (args.length) {
            case 1 -> {
                if (Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]) != null) {
                    Logger.log("Information of service group '" + args[0] + "':", LogType.INFO);
                    Logger.log(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]).getInformations());

                    Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutGroupReload(args[0]));
                } else {
                    Logger.log("Terium could't find a service-group with name '" + args[0] + "'.", LogType.ERROR);
                }
            }
            case 3 -> {
                if (Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]) != null) {
                    if(args[1].equalsIgnoreCase("template")) {
                        if(Terium.getTerium().getTemplateManager().getTemplateByName(args[2]) != null) {
                            Terium.getTerium().getServiceGroupManager().updateServiceGroup(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]), args[1], args[2]);
                        } else {
                            Logger.log("Terium could't find a template with name '" + args[2] + "'.", LogType.ERROR);
                        }
                    }

                    if (args[1].equalsIgnoreCase("maximum_players") || args[1].equalsIgnoreCase("memory") || args[1].equalsIgnoreCase("minimal_services") || args[1].equalsIgnoreCase("maximal_services") || args[1].equalsIgnoreCase("port")) {
                        Terium.getTerium().getServiceGroupManager().updateServiceGroup(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]), args[1], Integer.parseInt(args[2]));
                        Logger.log("Successfully updated service group '" + args[0] + "'.", LogType.INFO);
                        return;
                    }

                    if (args[1].equalsIgnoreCase("maintenance")) {
                        Terium.getTerium().getServiceGroupManager().updateServiceGroup(Terium.getTerium().getServiceGroupManager().getServiceGroupByName(args[0]), args[1], Boolean.parseBoolean(args[2]));
                        Logger.log("Successfully updated service group '" + args[0] + "'.", LogType.INFO);
                    }

                    Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutGroupReload(args[0]));
                } else {
                    Logger.log("Terium could't find a service-group with name '" + args[0] + "'.", LogType.ERROR);
                }
            }
            default -> {
                Logger.log("Syntax: edit-group <group_name> <maintenance/maximum_players/memory/minimal_services/maximal_services> <value>", LogType.INFO);
            }
        }
    }
}
