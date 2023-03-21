package cloud.terium.cloudsystem.node.console.commands;

import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.console.command.Command;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Stop the cloud-system", "exit");
    }

    @Override
    public void execute(String[] args) {
        NodeStartup.getNode().shutdownCloud();
    }
}