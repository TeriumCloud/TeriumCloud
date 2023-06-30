package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

import java.util.List;

public class StartCommand extends Command {

    public StartCommand() {
        super("start", "Starting a cloud service by group and count", "create");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            ClusterStartup.getCluster().getServiceGroupProvider().getServiceGroupByName(args[0]).ifPresentOrElse(serviceGroup -> ClusterStartup.getCluster().getServiceFactory().createService(serviceGroup), () -> Logger.log("A service group with that name isn't registered.", LogType.ERROR));
            return;
        }

        Logger.log("start [group] | start a service by group", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1)
            return ClusterStartup.getCluster().getServiceGroupProvider().getAllServiceGroups().stream().map(ICloudServiceGroup::getGroupName).toList();

        return super.tabComplete(args);
    }
}
