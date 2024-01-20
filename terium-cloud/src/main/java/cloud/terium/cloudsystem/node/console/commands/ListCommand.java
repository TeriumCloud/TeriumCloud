package cloud.terium.cloudsystem.node.console.commands;

import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;

public class ListCommand extends Command {

    public ListCommand() {
        super("list", "List of all services");
    }

    @Override
    public void execute(String[] args) {
        Logger.log("All active cloud services:", LogType.INFO);
        Logger.log("", LogType.INFO);
        NodeStartup.getNode().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
            Logger.log("Services from group '§b" + group.getGroupName() + "§f':", LogType.INFO);
            NodeStartup.getNode().getServiceProvider().getServicesByGroupName(group.getGroupName()).forEach(service ->
                    Logger.log(" §7● §fName: " + service.getServiceName() + " | State: " + service.getServiceState() + " | Players: " + service.getOnlinePlayers() + "/" + service.getMaxPlayers(), LogType.INFO));

            Logger.log("", LogType.INFO);
        });
    }
}
