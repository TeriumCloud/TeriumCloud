package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.config.CloudConfig;
import cloud.terium.cloudsystem.config.ConfigManager;
import cloud.terium.cloudsystem.console.CommandManager;
import cloud.terium.cloudsystem.console.ConsoleManager;
import cloud.terium.cloudsystem.event.EventProvider;
import cloud.terium.cloudsystem.group.ServiceGroupFactory;
import cloud.terium.cloudsystem.group.ServiceGroupProvider;
import cloud.terium.cloudsystem.module.ModuleProvider;
import cloud.terium.cloudsystem.node.Node;
import cloud.terium.cloudsystem.node.NodeFactory;
import cloud.terium.cloudsystem.node.NodeProvider;
import cloud.terium.cloudsystem.pipe.TeriumNetworkProvider;
import cloud.terium.cloudsystem.service.CloudServiceProvider;
import cloud.terium.cloudsystem.template.TemplateFactory;
import cloud.terium.cloudsystem.template.TemplateProvider;
import cloud.terium.cloudsystem.utils.CloudUtils;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.api.ICloudFactory;
import cloud.terium.teriumapi.api.ICloudProvider;
import cloud.terium.teriumapi.console.IConsoleProvider;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.module.IModuleProvider;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeFactory;
import cloud.terium.teriumapi.node.INodeProvider;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import sun.misc.Signal;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TeriumCloud extends TeriumAPI {

    private static TeriumCloud terium;
    private final CloudUtils cloudUtils;
    private final CommandManager commandManager;
    private final CloudConfig cloudConfig;
    private final ConfigManager configManager;
    private final ConsoleManager consoleManager;
    private final TeriumNetworkProvider networking;
    private final NodeProvider nodeProvider;
    private final NodeFactory nodeFactory;
    private final ServiceGroupProvider serviceGroupProvider;
    private final ServiceGroupFactory serviceGroupFactory;
    private final CloudServiceProvider serviceProvider;
    private final EventProvider eventProvider;
    private final TemplateProvider templateProvider;
    private final ModuleProvider moduleProvider;
    private final TemplateFactory templateFactory;
    private final INode thisNode;

    public static void main(String[] args) {
        new TeriumCloud();
    }

    @SneakyThrows
    public TeriumCloud() {
        terium = this;
        this.cloudUtils = new CloudUtils();

        System.setProperty("org.jline.terminal.dumb", "true");
        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b_______         _____  _     _ ______\s
                §f   |    |______ |_____/   |   |     | |  |  | §b|       |      |     | |     | |     \\
                §f   |    |______ |    \\_ __|__ |_____| |  |  | §b|_____  |_____ |_____| |_____| |_____/ §7[§f%version%§7]
                                                                                            \s
                §7> §fTerium by ByRaudy(Jannik H.)\s
                §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                """.replace("%version%", getCloudUtils().getVersion()));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "Welcome to terium-cloud!"));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "terium-cloud is starting phase §6one §7of the startup... Please wait a small while."));

        this.configManager = new ConfigManager();
        this.cloudConfig = configManager.toCloudConfig();
        this.networking = new TeriumNetworkProvider();
        this.eventProvider = new EventProvider();
        this.templateProvider = new TemplateProvider();
        this.templateFactory = new TemplateFactory();
        this.nodeProvider = new NodeProvider();
        this.nodeFactory = new NodeFactory();
        this.thisNode = new Node(cloudConfig.name(), "", new InetSocketAddress(cloudConfig.ip(), cloudConfig.port()));
        this.nodeProvider.registerNodes();
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.serviceProvider = new CloudServiceProvider();
        this.moduleProvider = new ModuleProvider();
        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);

        Logger.log("Starting phase §6two §fof the startup...", LogType.INFO);
        Thread.sleep(1000);

        consoleManager.clearScreen();
        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b_______         _____  _     _ ______\s
                §f   |    |______ |_____/   |   |     | |  |  | §b|       |      |     | |     | |     \\
                §f   |    |______ |    \\_ __|__ |_____| |  |  | §b|_____  |_____ |_____| |_____| |_____/ §7[§f%version%§7]
                                                                                            \s
                §7> §fTerium by ByRaudy(Jannik H.)\s
                §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                
                 §a> §fLoaded %commands% commands successfully.
                 §a> §fLoaded %templates% templates successfully.
                 §a> §fLoaded %groups% groups successfully.
                 §a> §fLoaded %loaded_nodes% and connected to %connected_nodes% nodes successfully.
                 §a> §fStarted terium-server on %ip%:%port%.
                
                """.replace("%version%", getCloudUtils().getVersion()).replace("%templates%", templateProvider.getAllTemplates().size() + "").replace("%commands%", commandManager.getBuildedCommands().keySet().size() + "")
                .replace("%ip%", cloudConfig.ip()).replace("%port%", cloudConfig.port() + "").replace("%loaded_nodes%", nodeProvider.getAllNodes().stream().filter(node -> node != TeriumCloud.getTerium().getThisNode()).toList().size() + "")
                .replace("%connected_nodes%", nodeProvider.getNodeClients().values().size() + "").replace("%groups%", serviceGroupProvider.getAllServiceGroups().size() + ""));
        this.moduleProvider.loadModules();

        Signal.handle(new Signal("INT"), signal -> {
            cloudUtils.setRunning(false);
            shutdownCloud();
        });
    }

    @Override
    public ICloudProvider getProvider() {
        return new ICloudProvider() {
            @Override
            public ICloudService getThisService() {
                return null;
            }

            @Override
            public INode getThisNode() {
                return thisNode;
            }

            @Override
            public ICloudServiceProvider getServiceProvider() {
                return serviceProvider;
            }

            @Override
            public ICloudServiceGroupProvider getServiceGroupProvider() {
                return serviceGroupProvider;
            }

            @Override
            public ICloudPlayerProvider getCloudPlayerProvider() {
                // Todo: need to be implemented
                return null;
            }

            @Override
            public IConsoleProvider getConsoleProvider() {
                return consoleManager;
            }

            @Override
            public IEventProvider getEventProvider() {
                return eventProvider;
            }

            @Override
            public IModuleProvider getModuleProvider() {
                return moduleProvider;
            }

            @Override
            public INodeProvider getNodeProvider() {
                return nodeProvider;
            }

            @Override
            public ITemplateProvider getTemplateProvider() {
                return templateProvider;
            }

            @Override
            public IDefaultTeriumNetworking getTeriumNetworking() {
                return networking;
            }
        };
    }

    @Override
    public ICloudFactory getFactory() {
        return new ICloudFactory() {
            @Override
            public ICloudServiceFactory getServiceFactory() {
                // Todo: need to be implemented
                return null;
            }

            @Override
            public ICloudServiceGroupFactory getServiceGroupFactory() {
                return serviceGroupFactory;
            }

            @Override
            public ITemplateFactory getTemplateFactory() {
                return templateFactory;
            }

            @Override
            public INodeFactory getNodeFactory() {
                return nodeFactory;
            }

            @Override
            public ICommandFactory getCommandFactory() {
                return commandManager;
            }
        };
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