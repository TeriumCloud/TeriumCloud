package cloud.terium.cloudsystem.manager;

import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.command.commands.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandManager {

    private final List<Command> commandList;

    public CommandManager() {
        this(false);
    }

    public CommandManager(boolean bridge) {
        this.commandList = new ArrayList<>();

        if (!bridge) {
            new HelpCommand(this);
            new StopCommand(this);
            new CreateGroupCommand(this);
            new EditGroupCommand(this);

            new ServiceCommand(this);
            new GroupCommand(this);
            new ListCommand(this);
            new ReloadCommand(this);
            new ListModulesCommand(this);
        }
    }

    public void register(Command... commands) {
        this.commandList.addAll(Arrays.asList(commands));
    }

    public Command getCommand(String name) {
        for (Command command : commandList) {
            if (command.getCommand().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }
}