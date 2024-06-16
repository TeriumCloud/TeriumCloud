package cloud.terium.extension.impl.console;

import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.console.command.ICommandFactory;

public class CommandFactory implements ICommandFactory {

    @Override
    public void registerCommand(Command command) {
        // NOT SUPPORTED FOR SERVER PROCESSES
        // ONLY MAIN PROCESS(CLOUD-SYSTEM) IS SUPPORTED
    }
}
