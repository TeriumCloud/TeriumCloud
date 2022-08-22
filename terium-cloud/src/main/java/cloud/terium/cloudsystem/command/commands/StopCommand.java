package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import lombok.SneakyThrows;

public class StopCommand extends Command {

    public StopCommand(CommandManager commandManager) {
        super("stop");
        commandManager.register(this);
    }

    @SneakyThrows
    @Override
    public void execute(String[] args) {
        Terium.getTerium().getCloudUtils().shutdownCloud();
    }
}
