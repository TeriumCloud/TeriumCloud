package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.List;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", "Shutdown a service", "stop");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(args[0]).ifPresentOrElse(ICloudService::shutdown, () -> {
                Logger.log("Cannot find a cloud service with that name.", LogType.ERROR);
            });

            return;
        }

        Logger.log("shutdown [service] | shutdown a service", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if(args.length == 1) {
            return TeriumCloud.getTerium().getServiceProvider().getAllCloudServices().stream().map(ICloudService::getServiceName).toList();
        }
        return super.tabComplete(args);
    }
}