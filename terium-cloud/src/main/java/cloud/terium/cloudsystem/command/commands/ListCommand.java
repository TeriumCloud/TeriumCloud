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
            Logger.log(group.name() + " (Title: " + group.groupTitle() + " / Type: " + group.serviceType() + ")", LogType.INFO);

            Terium.getTerium().getServiceManager().getMinecraftServices().forEach(service -> {
                if (service.getDefaultServiceGroup().equals(group)) {
                    i.getAndIncrement();

                    Logger.log(i.get() + ". " + service.serviceName() + " / Port: " + service.getPort() + " / Online: " + service.online(), LogType.INFO);
                }
            });
            Logger.log(" ", LogType.INFO);
        });
    }
}
