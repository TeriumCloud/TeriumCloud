package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.console.CommandManager;
import cloud.terium.cloudsystem.console.ConsoleManager;
import cloud.terium.cloudsystem.event.EventProvider;
import cloud.terium.cloudsystem.utils.CloudUtils;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import lombok.Getter;
import sun.misc.Signal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TeriumCloud {

    private static TeriumCloud terium;
    private final CloudUtils cloudUtils;
    private final CommandManager commandManager;
    private final ConsoleManager consoleManager;
    private final EventProvider eventProvider;

    public static void main(String[] args) {
        new TeriumCloud();
    }

    public TeriumCloud() {
        terium = this;
        this.cloudUtils = new CloudUtils();

        System.setProperty("org.jline.terminal.dumb", "true");
        Logger.log("""
                        \u001B[0m_______ _______  ______ _____ _     _ _______ \u001B[36m_______         _____  _     _ ______\s
                        \u001B[0m   |    |______ |_____/   |   |     | |  |  | \u001B[36m|       |      |     | |     | |     \\
                        \u001B[0m   |    |______ |    \\_ __|__ |_____| |  |  | \u001B[36m|_____  |_____ |_____| |_____| |_____/ \u001B[37m[\u001B[0m%version%\u001B[37m]
                                                                                                    \s
                        \u001B[0m> Terium by ByRaudy(Jannik H.)\s
                        \u001B[0m> Discord: \u001B[36mterium.cloud/discord \u001B[0m| Twitter: \u001B[36m@teriumcloud\u001B[0m
                        """.replace("%version%", getCloudUtils().getVersion()));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "Trying to start Terium..."));

        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);
        this.eventProvider = new EventProvider();

        Signal.handle(new Signal("INT"), signal -> {
            cloudUtils.setRunning(false);
            cloudUtils.shutdownCloud();
        });
    }

    public static TeriumCloud getTerium() {
        return terium;
    }
}