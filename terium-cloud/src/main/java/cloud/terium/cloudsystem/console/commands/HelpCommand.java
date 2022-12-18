package cloud.terium.cloudsystem.console.commands;

import cloud.terium.teriumapi.console.command.Command;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", new String[]{"?"}, new String[]{});
    }

    @Override
    public void execute(String[] args) {

    }
}
