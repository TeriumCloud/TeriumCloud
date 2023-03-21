package cloud.terium.cloudsystem.node.console;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.common.utils.logger.LoggerColors;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.console.IConsoleProvider;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Getter
public class ConsoleManager implements IConsoleProvider {

    private final Terminal terminal;
    private LineReader lineReader;
    private AggregateCompleter completer;
    private Thread thread;

    @SneakyThrows
    public ConsoleManager(CommandManager commandManager) {
        this.terminal = TerminalBuilder.builder()
                .name("terium-console")
                .system(true).streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8).dumb(true).build();

        readConsole();
    }

    public void readConsole() {
        Logger.log("Starting console-read thread...", LogType.INFO);
        completer = new AggregateCompleter(new ConsoleCompleter());

        this.lineReader = LineReaderBuilder.builder()
                .appName("terium-console")
                .terminal(terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_REMOVE_SLASH, false)
                .option(LineReader.Option.INSERT_TAB, false)
                .completer(completer)
                .build();

        this.thread = new Thread(() -> {
            while (true) {
                String input = null;
                Command command;
                if (TeriumCloud.getTerium().getCloudUtils().isRunning()) {
                    try {
                        input = lineReader.readLine(LoggerColors.replaceColorCodes(NodeStartup.getNode().getNodeConfig().promt().replace("%user%", username())
                                .replace("%node%", NodeStartup.getNode().getThisNode().getName()).replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion())));
                    } catch (EndOfFileException exception) {
                        input = lineReader.readLine("");
                    }
                } else {
                    input = lineReader.readLine("");
                }

                try {
                    if ((command = NodeStartup.getNode().getCommandManager().getCommand(input.split(" ")[0])) != null || (command = NodeStartup.getNode().getCommandManager().getCommandByAlias(input.split(" ")[0])) != null) {
                        final String[] args = input.split(" ");
                        command.execute(Arrays.copyOfRange(args, 1, args.length));
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Logger.log("terium-cloud found a error: " + exception.getMessage(), LogType.ERROR);
                }
            }
        });
        this.thread.start();
        Logger.log("Successfully started console-read thread.", LogType.INFO);
    }

    public void clearScreen() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
    }

    @SneakyThrows
    private String username() {
        return System.getProperty("user.name");
    }

    @Override
    public void sendConsole(String message, LogType logType) {
        Logger.log(message, logType);
    }
}