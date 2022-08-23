package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.manager.ConfigManager;
import cloud.terium.cloudsystem.manager.ConsoleManager;
import cloud.terium.cloudsystem.networking.DefaultTeriumNetworking;
import cloud.terium.cloudsystem.service.ServiceManager;
import cloud.terium.cloudsystem.service.group.ServiceGroupManager;
import cloud.terium.cloudsystem.template.TemplateManager;
import cloud.terium.cloudsystem.utils.CloudUtils;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import sun.misc.Signal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Terium {

    private static Terium terium;
    private final CloudUtils cloudUtils;
    private ConfigManager configManager;
    private final CommandManager commandManager;
    private final ConsoleManager consoleManager;
    private final ServiceManager serviceManager;
    private final ServiceGroupManager serviceGroupManager;
    private final DefaultTeriumNetworking defaultTeriumNetworking;

    public static void main(String[] args) {
        new Terium();
    }

    public Terium() {
        terium = this;
        this.cloudUtils = new CloudUtils();
        this.configManager = new ConfigManager();

        System.out.println(cloudUtils.getStartMessage());
        this.cloudUtils.checkLicense();

        System.out.println(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "Trying to start Terium..."));

        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);
        this.serviceManager = new ServiceManager();
        this.serviceGroupManager = new ServiceGroupManager();
        this.defaultTeriumNetworking = new DefaultTeriumNetworking(configManager);

        new TemplateManager();

        Signal.handle(new Signal("INT"), signal -> cloudUtils.shutdownCloud());

        Logger.log("Successfully started Terium.", LogType.INFO);
        // serviceManager.startCloudServices();
        serviceManager.startServiceCheck();
    }

    public static Terium getTerium() {
        return terium;
    }
}