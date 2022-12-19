package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;

import java.util.Arrays;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Stop the cloud-system", new String[]{"exit", "shutdown"}, null);
    }

    @Override
    public void execute(String[] args) {
        TeriumCloud.getTerium().getCloudUtils().shutdownCloud();
    }
}