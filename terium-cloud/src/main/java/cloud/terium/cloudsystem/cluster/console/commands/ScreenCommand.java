package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.service.CloudService;
import cloud.terium.cloudsystem.cluster.utils.Logger;
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
        if (args.length == 1) {
            ClusterStartup.getCluster().getServiceProvider().getServiceByName(args[0]).ifPresentOrElse(cloudService -> ((CloudService) cloudService).toggleScreen(), () -> Logger.log("A screen with that name isn't registered.", LogType.ERROR));
            return;
        }

        Logger.log("screen [service] | toggle logging of a service console", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1)
            return ClusterStartup.getCluster().getServiceProvider().getAllServices().stream().filter(cloudService -> cloudService.getServiceNode().getName().equals(ClusterStartup.getCluster().getProvider().getThisNode().getName())).map(ICloudService::getServiceName).toList();
        return List.of();
    }
}