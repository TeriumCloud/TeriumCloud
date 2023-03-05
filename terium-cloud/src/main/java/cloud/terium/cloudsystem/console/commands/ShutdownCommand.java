package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.Arrays;
import java.util.List;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", "Shutdown a service", "stop");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            if(args[0].equals("service")) {
                TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(args[1]).ifPresentOrElse(ICloudService::shutdown, () -> {
                    Logger.log("Cannot find a cloud service with that name.", LogType.ERROR);
                });
            }

            if(args[0].equals("group")) {
                TeriumCloud.getTerium().getServiceProvider().getCloudServicesByGroupName(args[1]).forEach(ICloudService::shutdown);
            }

            return;
        }

        Logger.log("shutdown service [service] | shutdown a service", LogType.INFO);
        Logger.log("shutdown group [group] | shutdown a service group", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if(args.length == 1) {
            return Arrays.asList("service", "group");
        }
        if(args.length == 2) {
            return args[0].equals("service") ? TeriumCloud.getTerium().getServiceProvider().getAllCloudServices().stream().map(ICloudService::getServiceName).toList() : TeriumCloud.getTerium().getServiceGroupProvider().getAllServiceGroups().stream().map(ICloudServiceGroup::getGroupName).toList();
        }
        return super.tabComplete(args);
    }
}