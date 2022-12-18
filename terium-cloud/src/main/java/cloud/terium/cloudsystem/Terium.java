package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.console.CommandManager;
import cloud.terium.cloudsystem.console.ConsoleManager;
import cloud.terium.cloudsystem.event.EventProvider;
import cloud.terium.cloudsystem.utils.CloudUtils;
import lombok.Getter;

@Getter
public class Terium {

    private static Terium terium;
    private final CloudUtils cloudUtils;
    private final CommandManager commandManager;
    private final ConsoleManager consoleManager;
    private final EventProvider eventProvider;

    public static void main(String[] args) {
        new Terium();
    }

    public Terium() {
        System.setProperty("org.jline.terminal.dumb", "true");

        terium = this;
        this.cloudUtils = new CloudUtils();
        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);
        this.eventProvider = new EventProvider();
    }

    public static Terium getTerium() {
        return terium;
    }
}