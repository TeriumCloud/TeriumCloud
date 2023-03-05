package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.service.CloudService;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.List;

public class ScreenCommand extends Command {

    public ScreenCommand() {
        super("screen", "Toggle logging of a service console");
    }

    @Override
    public void execute(String[] args) {
        if(args.length == 1) {
            TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(args[0]).ifPresentOrElse(cloudService -> ((CloudService)cloudService).toggleScreen(), () -> Logger.log("A screen with that same isn't registered.", LogType.ERROR));
            return;
        }

        Logger.log("screen [service] | toggle logging of a service console");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return TeriumCloud.getTerium().getServiceProvider().getAllCloudServices().stream().map(ICloudService::getServiceName).toList();
    }
}