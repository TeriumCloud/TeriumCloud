package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.console.CommandManager;
import cloud.terium.cloudsystem.console.ConsoleManager;
import cloud.terium.cloudsystem.event.EventProvider;
import cloud.terium.cloudsystem.utils.CloudUtils;
import lombok.Getter;
import sun.misc.Signal;

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
        System.out.println("""
                        \u001B[0m_______ _______  ______ _____ _     _ _______ \u001B[36m_______         _____  _     _ ______\s
                        \u001B[0m   |    |______ |_____/   |   |     | |  |  | \u001B[36m|       |      |     | |     | |     \\
                        \u001B[0m   |    |______ |    \\_ __|__ |_____| |  |  | \u001B[36m|_____  |_____ |_____| |_____| |_____/
                                                                                                    \s
                        \u001B[0m> Terium by ByRaudy(Jannik H.)\s
                        \u001B[0m> Discord: terium.cloud/discord | Twitter: \u001B[36m@teriumcloud \u001B[0m
                        """);

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