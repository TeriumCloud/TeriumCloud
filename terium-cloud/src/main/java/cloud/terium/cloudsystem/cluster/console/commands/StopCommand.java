package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.teriumapi.console.command.Command;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Stop the cloud-system", "exit");
    }

    @Override
    public void execute(String[] args) {
        ClusterStartup.getCluster().shutdownCloud();
    }
}