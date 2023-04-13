package cloud.terium.cloudsystem.node.console;

import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.node.console.commands.*;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
public class CommandManager implements ICommandFactory {

    private final List<Command> commandList;
    private final HashMap<String, Command> buildedCommands;

    public CommandManager() {
        this.commandList = new ArrayList<>();
        this.buildedCommands = new HashMap<>();

        Logger.log("Loading all commands...", LogType.INFO);
        registerCommand(new HelpCommand());
        registerCommand(new StopCommand());
        registerCommand(new ClearCommand());
        registerCommand(new ModuleCommand());
        registerCommand(new ListCommand());
        registerCommand(new ScreenCommand());
        registerCommand(new ReloadCommand());
        Logger.log("Successfully loaded all commands...", LogType.INFO);
    }

    public void registerCommand(Command command) {
        this.commandList.remove(getCommand(command.getCommand()));
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
        buildedCommands.remove(command.getCommand());

        buildedCommands.put(command.getCommand(), command);
        if (command.getAliases() != null) {
            for (String alias : command.getAliases()) {
                buildedCommands.remove(alias);
                buildedCommands.put(alias, command);
            }
        }
        Logger.log("Loaded commmand '" + command.getCommand() + "'", LogType.INFO);
    }

    /*private void buildCommand(Command command) {
        buildedCommands.remove(command.getCommand());

        List<Completer> arguments = new ArrayList<>();
        arguments.add(new StringsCompleter(command.getCommand()));
        if (command.getArguments() != null)
            command.getArguments().forEach(strings -> arguments.add(new StringsCompleter(strings)));
        arguments.add(NullCompleter.INSTANCE);

        buildedCommands.put(command.getCommand(), new ArgumentCompleter(arguments));
        if (command.getAliases() != null) {
            for (String alias : command.getAliases()) {
                buildedCommands.remove(alias);
                arguments.clear();
                arguments.add(new StringsCompleter(alias));
                if (command.getArguments() != null)
                    command.getArguments().forEach(strings -> arguments.add(new StringsCompleter(strings)));
                arguments.add(NullCompleter.INSTANCE);
                buildedCommands.put(alias, new ArgumentCompleter(arguments));
            }
        }
    }*/
}