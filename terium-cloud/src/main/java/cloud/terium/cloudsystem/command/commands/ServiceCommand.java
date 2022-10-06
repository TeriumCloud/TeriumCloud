package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packets.PacketPlayOutServiceLock;
import cloud.terium.networking.packets.PacketPlayOutServiceUnlock;
import cloud.terium.teriumapi.service.CloudServiceState;
import cloud.terium.teriumapi.service.ICloudService;

public class ServiceCommand extends Command {

    public ServiceCommand(CommandManager commandManager) {
        super("service");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            if (Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]) != null) {
                ICloudService iCloudService = Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]);

                switch (args[1]) {
                    case "info" -> {
                        Logger.log("Information of service '" + iCloudService.getServiceName() + "':", LogType.INFO);
                        Logger.log("{   ");
                        Logger.log("    Name: " + iCloudService.getServiceName());
                        Logger.log("    Id: #" + (iCloudService.getServiceId() < 10 ? "0" + iCloudService.getServiceId() : iCloudService.getServiceId()));
                        Logger.log("    State: " + iCloudService.getServiceState());
                        Logger.log("    Group: " + iCloudService.getServiceGroup().getServiceGroupName());
                        Logger.log("    Type: " + iCloudService.getServiceType());
                        Logger.log("    Locked: " + iCloudService.isLocked());
                        Logger.log("    Used memory: " + iCloudService.getUsedMemory());
                        Logger.log("    Max memory: " + iCloudService.getMaxMemory());
                        Logger.log("    Online players: " + iCloudService.getOnlinePlayers());
                        Logger.log("    Max players: " + iCloudService.getMaxPlayers());
                        Logger.log("}   ");
                    }
                    case "shutdown" -> iCloudService.shutdown();
                    case "lock" -> {
                        if (iCloudService.isLocked()) {
                            Logger.log("This service is already locked!", LogType.ERROR);
                            return;
                        }

                        iCloudService.setLocked(true);
                        Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceLock(iCloudService.getServiceName()));
                        Logger.log("Successfully locked service " + iCloudService.getServiceName(), LogType.INFO);
                    }
                    case "unlock" -> {
                        if (!iCloudService.isLocked()) {
                            Logger.log("This service isn't locked yet!", LogType.ERROR);
                            return;
                        }

                        iCloudService.setLocked(false);
                        Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceUnlock(iCloudService.getServiceName()));
                        Logger.log("Successfully unlocked service " + iCloudService.getServiceName(), LogType.INFO);
                    }
                    case "state" ->
                            Logger.log("Syntax: service <service> info|shutdown|state<state>|lock|unlock", LogType.INFO);
                }
            } else {
                Logger.log("Terium could't find a service with name '" + args[0] + "'.", LogType.ERROR);
            }
            return;
        }

        if (args.length == 3) {
            if (args[1].equals("state"))
                if (Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]) != null) {
                    ICloudService iCloudService = Terium.getTerium().getServiceManager().getCloudServiceByName(args[0]);


                }
        } else {
            Logger.log("Terium could't find a service with name '" + args[0] + "'.", LogType.ERROR);
        }

        if (args.length < 2) {
            Logger.log("Syntax: service <service> info|shutdown|state<state>|lock|unlock", LogType.INFO);
        }
    }
}