package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.config.CloudConfig;
import cloud.terium.cloudsystem.config.ConfigManager;
import cloud.terium.cloudsystem.console.CommandManager;
import cloud.terium.cloudsystem.console.ConsoleManager;
import cloud.terium.cloudsystem.event.EventProvider;
import cloud.terium.cloudsystem.template.TemplateFactory;
import cloud.terium.cloudsystem.template.TemplateProvider;
import cloud.terium.cloudsystem.utils.CloudUtils;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import sun.misc.Signal;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TeriumCloud {

    private static TeriumCloud terium;
    private final CloudUtils cloudUtils;
    private final CommandManager commandManager;
    private final CloudConfig cloudConfig;
    private final ConfigManager configManager;
    private final ConsoleManager consoleManager;
    private final EventProvider eventProvider;
    private final TemplateProvider templateProvider;
    private final TemplateFactory templateFactory;

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

        this.configManager = new ConfigManager();
        this.cloudConfig = configManager.toCloudConfig();
        this.eventProvider = new EventProvider();
        this.templateProvider = new TemplateProvider();
        this.templateFactory = new TemplateFactory();
        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);

        Signal.handle(new Signal("INT"), signal -> {
            cloudUtils.setRunning(false);
            shutdownCloud();
        });
    }

    public static TeriumCloud getTerium() {
        return terium;
    }

    @SneakyThrows
    public void shutdownCloud() {
        Logger.log("Trying to stop terium-cloud...", LogType.INFO);

        TeriumCloud.getTerium().getCloudUtils().setRunning(false);
        //TeriumCloud.getTerium().getServiceManager().getMinecraftServices().forEach(ICloudService::shutdown);
        Logger.log("Successfully stopped all services.", LogType.INFO);
        Thread.sleep(1000);
        //TeriumCloud.getTerium().getDefaultTeriumNetworking().getServer().getChannel().close().sync();
        Logger.log("Successfully stopped terium-server.", LogType.INFO);
        // TeriumCloud.getTerium().getConfigManager().resetPort();
        Logger.log("Successfully reset terium-port.", LogType.INFO);

        FileUtils.deleteDirectory(new File("servers//"));
        Logger.log("Successfully deleted server folder.", LogType.INFO);
        FileUtils.deleteDirectory(new File("data//cache//"));
        Logger.log("Successfully deleted data/cache folder.", LogType.INFO);
        Thread.sleep(300);
        Logger.log("Successfully stopped terium-cloud. Goodbye!", LogType.INFO);
        Thread.sleep(1000);
        System.exit(0);
    }
}