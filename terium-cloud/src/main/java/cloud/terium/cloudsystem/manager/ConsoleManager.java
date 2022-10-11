package cloud.terium.cloudsystem.manager;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

@Getter
public class ConsoleManager {

    private final Terminal terminal;
    private LineReader lineReader;
    private final String username;
    private CommandManager commandManager;
    private Thread thread;

    @SneakyThrows
    public ConsoleManager(CommandManager commandManager) {
        this.terminal = TerminalBuilder.terminal();
        this.username = username();

        readConsole(commandManager);
    }

    public void readConsole(CommandManager commandManager) {
        this.commandManager = commandManager;
        List<String> autoComplete = new Vector<String>();
        commandManager.getCommandList().forEach(command -> {
            autoComplete.add(command.getCommand());
        });
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).completer(new ArgumentCompleter(new StringsCompleter(autoComplete))).build();

        this.thread = new Thread(() -> {
            while (true) {
                String input = null;
                final Command command;
                if (Terium.getTerium().getCloudUtils().isRunning()) {
                    try {
                        input = lineReader.readLine("\u001B[36m" + username + "\u001B[0m@terium => ");
                    } catch (EndOfFileException exception) {
                        input = lineReader.readLine("");
                    }
                } else {
                    input = lineReader.readLine("");
                }

                try {
                    if ((command = commandManager.getCommand(input.split(" ")[0])) != null) {
                        final String[] args = input.split(" ");
                        command.execute(Arrays.copyOfRange(args, 1, args.length));
                    } else {
                        Logger.log("This command doesn't exist!", LogType.ERROR);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Logger.log("Terium found a error: " + exception.getMessage(), LogType.ERROR);
                }
            }
        });
        this.thread.start();
    }

    @SneakyThrows
    private String username() {
        return System.getProperty("user.name");
    }
}