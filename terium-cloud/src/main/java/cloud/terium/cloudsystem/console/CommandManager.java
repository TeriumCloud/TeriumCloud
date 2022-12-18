package cloud.terium.cloudsystem.console.command;

import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager implements ICommandFactory {

    private final List<Command> commandList;

    public CommandManager() {
        this.commandList = new ArrayList<>();
    }

    public void registerCommand(Command command) {
        this.commandList.add(command);
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