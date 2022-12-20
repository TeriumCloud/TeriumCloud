package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.teriumapi.console.command.Command;
import org.jline.builtins.Completers;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Stop the cloud-system", new String[]{"exit", "shutdown"});
    }

    @Override
    public void execute(String[] args) {
        TeriumCloud.getTerium().shutdownCloud();
    }
}