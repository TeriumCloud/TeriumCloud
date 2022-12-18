package cloud.terium.cloudsystem.console;

import cloud.terium.cloudsystem.console.commands.HelpCommand;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import lombok.Getter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandManager implements ICommandFactory {

    private final List<Command> commandList;
    private final List<Completer> buildedCommands;

    public CommandManager() {
        this.commandList = new ArrayList<>();
        this.buildedCommands = new ArrayList<>();

        registerCommand(new HelpCommand(Arrays.asList(new String[]{"service"}, new String[]{"-test3", "-test4"})));
    }

    public void registerCommand(Command command) {
        this.commandList.add(command);
        buildCommand(command);
    }

    public Command getCommand(String name) {
        for (Command command : commandList) {
            if (command.getCommand().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    public Command getCommandByAlias(String alias) {
        for (Command command : commandList) {
            if (Arrays.stream(command.getAliases()).toList().contains(alias)) {
                return command;
            }
        }
        return null;
    }

    private void buildCommand(Command command) {
        List<Completer> arguments = new ArrayList<>();
        arguments.add(new StringsCompleter(command.getCommand()));
        command.getArguments().forEach(strings -> arguments.add(new StringsCompleter(strings)));
        arguments.add(NullCompleter.INSTANCE);

        buildedCommands.add(new ArgumentCompleter(arguments));
        for (String alias : command.getAliases()) {
            arguments.clear();
            arguments.add(new StringsCompleter(alias));
            command.getArguments().forEach(strings -> arguments.add(new StringsCompleter(strings)));
            arguments.add(NullCompleter.INSTANCE);
            buildedCommands.add(new ArgumentCompleter(arguments));
        }
    }
}