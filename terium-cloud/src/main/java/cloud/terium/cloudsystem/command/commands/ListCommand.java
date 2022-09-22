package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class ListCommand extends Command {

    public ListCommand(CommandManager commandManager) {
        super("list");
        commandManager.register(this);
    }

    @Override
    public void execute(String[] args) {
        Logger.log("List of all services: ", LogType.INFO);
        System.out.println(" ");
        Terium.getTerium().getServiceGroupManager().getServiceGroups().forEach(group -> {
            AtomicInteger i = new AtomicInteger();
            Logger.log(group.getServiceGroupName() + " (Title: " + group.getGroupTitle() + " / Type: " + group.getServiceType() + ")", LogType.INFO);

            Terium.getTerium().getServiceManager().getMinecraftServices().forEach(service -> {
                if (service.getServiceGroup().equals(group)) {
                    i.getAndIncrement();

                    Logger.log(i.get() + ". " + service.getServiceName() + " / Memory: " + service.getUsedMemory() + "mb/" + group.getMemory() + "mb / Players: " + service.getOnlinePlayers() + "/" + service.getMaxPlayers() + " / Port: " + service.getPort() + " / State: " + service.getServiceState(), LogType.INFO);
                }
            });
            Logger.log(" ", LogType.INFO);
        });
    }
}
