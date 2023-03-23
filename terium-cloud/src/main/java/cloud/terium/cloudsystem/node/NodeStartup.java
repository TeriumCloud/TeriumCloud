package cloud.terium.cloudsystem.node;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.common.event.EventProvider;
import cloud.terium.cloudsystem.common.screen.ScreenProvider;
import cloud.terium.cloudsystem.node.config.ConfigManager;
import cloud.terium.cloudsystem.node.config.NodeConfig;
import cloud.terium.cloudsystem.node.console.CommandManager;
import cloud.terium.cloudsystem.node.console.ConsoleListener;
import cloud.terium.cloudsystem.node.console.ConsoleManager;
import cloud.terium.cloudsystem.node.entity.CloudPlayerListener;
import cloud.terium.cloudsystem.node.entity.CloudPlayerProvider;
import cloud.terium.cloudsystem.node.module.ModuleProvider;
import cloud.terium.cloudsystem.node.node.Node;
import cloud.terium.cloudsystem.node.node.NodeFactory;
import cloud.terium.cloudsystem.node.node.NodeProvider;
import cloud.terium.cloudsystem.node.pipe.TeriumNetworkProvider;
import cloud.terium.cloudsystem.node.service.CloudServiceFactory;
import cloud.terium.cloudsystem.node.service.CloudServiceListener;
import cloud.terium.cloudsystem.node.service.CloudServiceProvider;
import cloud.terium.cloudsystem.node.service.group.ServiceGroupFactory;
import cloud.terium.cloudsystem.node.service.group.ServiceGroupProvider;
import cloud.terium.cloudsystem.node.template.TemplateFactory;
import cloud.terium.cloudsystem.node.template.TemplateProvider;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.networking.packet.node.PacketPlayOutNodeStarted;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.api.ICloudFactory;
import cloud.terium.teriumapi.api.ICloudProvider;
import cloud.terium.teriumapi.console.IConsoleProvider;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import cloud.terium.teriumapi.entity.ICloudPlayerProvider;
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.module.IModuleProvider;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeFactory;
import cloud.terium.teriumapi.node.INodeProvider;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.ICloudServiceProvider;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import sun.misc.Signal;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class NodeStartup extends TeriumAPI {

    private static NodeStartup node;
    private final CommandManager commandManager;
    private NodeConfig nodeConfig;
    private ConfigManager configManager;
    private final ConsoleManager consoleManager;
    private final TeriumNetworkProvider networking;
    private final NodeProvider nodeProvider;
    private final NodeFactory nodeFactory;
    private final ServiceGroupProvider serviceGroupProvider;
    private final ServiceGroupFactory serviceGroupFactory;
    private final CloudServiceProvider serviceProvider;
    private final CloudServiceFactory serviceFactory;
    private final CloudPlayerProvider cloudPlayerProvider;
    private final ScreenProvider screenProvider;
    private final EventProvider eventProvider;
    private final TemplateProvider templateProvider;
    private final ModuleProvider moduleProvider;
    private final TemplateFactory templateFactory;
    private final INode thisNode;

    @SneakyThrows
    public NodeStartup() {
        node = this;
        TeriumCloud.getTerium().getCloudUtils();
        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b__   _  _____  ______  _______
                 §f  |    |______ |_____/   |   |     | |  |  | §b| \\  | |     | |     \\ |______
                 §f  |    |______ |    \\_ __|__ |_____| |  |  | §b|  \\_| |_____| |_____/ |______ §7[§f%version%§7]
                                                                                                 \s
                 §7> §fTerium by ByRaudy(Jannik H.)\s
                 §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                 """.replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion()));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "Welcome to terium-cloud!"));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "terium-cloud is starting phase §6one §7of the startup... Please wait a small while."));

        this.configManager = new ConfigManager();
        this.nodeConfig = configManager.toNodeConfig();
        this.networking = new TeriumNetworkProvider();

        this.eventProvider = new EventProvider();
        this.eventProvider.subscribeListener(new ConsoleListener());
        this.eventProvider.subscribeListener(new CloudPlayerListener());
        this.eventProvider.subscribeListener(new CloudServiceListener());

        this.templateProvider = new TemplateProvider();
        this.templateFactory = new TemplateFactory();
        this.nodeProvider = new NodeProvider();
        this.nodeFactory = new NodeFactory();
        this.thisNode = new Node(nodeConfig.name(), "", new InetSocketAddress(nodeConfig.ip(), nodeConfig.port()));
        this.nodeProvider.registerNodes();
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.serviceProvider = new CloudServiceProvider();
        this.serviceFactory = new CloudServiceFactory();
        this.cloudPlayerProvider = new CloudPlayerProvider();
        this.screenProvider = new ScreenProvider();
        this.moduleProvider = new ModuleProvider();
        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);

        Logger.log("Starting phase §6two §fof the startup...", LogType.INFO);
        Thread.sleep(1000);

        consoleManager.clearScreen();
        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b__   _  _____  ______  _______
                 §f  |    |______ |_____/   |   |     | |  |  | §b| \\  | |     | |     \\ |______
                 §f  |    |______ |    \\_ __|__ |_____| |  |  | §b|  \\_| |_____| |_____/ |______ §7[§f%version%§7]
                                                                                                 \s
                 §7> §fTerium by ByRaudy(Jannik H.)\s
                 §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                                 
                  §a> §fLoaded %commands% commands successfully.
                  §a> §fLoaded %templates% templates successfully.
                  §a> §fLoaded %groups% groups successfully.
                  §a> §fStarted terium-client on %ip%:%port%.
                  §a> §fConnected with terium-server on %master-ip%:%master-port%.
                                 
                 """.replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion()).replace("%templates%", templateProvider.getAllTemplates().size() + "").replace("%commands%", commandManager.getBuildedCommands().keySet().size() + "")
                .replace("%ip%", nodeConfig.ip()).replace("%port%", nodeConfig.port() + "").replace("%groups%", serviceGroupProvider.getAllServiceGroups().size() + ""));
        this.moduleProvider.loadModules();
        this.networking.sendPacket(new PacketPlayOutNodeStarted(thisNode.getName(), nodeConfig.master().get("key").getAsString()));

        Signal.handle(new Signal("INT"), signal -> {
            TeriumCloud.getTerium().getCloudUtils().setRunning(false);
            shutdownCloud();
        });

        serviceProvider.startServiceCheck();
        serviceProvider.startServiceStopCheck();
    }

    public static NodeStartup getNode() {
        return node;
    }

    public boolean isDebugMode() {
        return nodeConfig.debugMode();
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
                return cloudPlayerProvider;
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

            @Override
            public String getVersion() {
                return TeriumCloud.getTerium().getCloudUtils().getVersion();
            }
        };
    }

    @Override
    public ICloudFactory getFactory() {
        return new ICloudFactory() {
            @Override
            public ICloudServiceFactory getServiceFactory() {
                return serviceFactory;
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

    @SneakyThrows
    public void shutdownCloud() {
        Logger.log("Trying to stop terium-cloud...", LogType.INFO);

        TeriumCloud.getTerium().getCloudUtils().setRunning(false);
        getServiceProvider().getAllCloudServices().forEach(ICloudService::shutdown);
        Thread.sleep(500);
        Logger.log("Successfully stopped all services.", LogType.INFO);
        Thread.sleep(1000);
        getNetworking().getChannel().close().sync();
        Logger.log("Successfully stopped terium-server.", LogType.INFO);

        FileUtils.deleteDirectory(new File("servers//"));
        Logger.log("Successfully deleted server folder.", LogType.INFO);
        Thread.sleep(300);
        Logger.log("Successfully stopped terium-cloud. Goodbye!", LogType.INFO);
        Thread.sleep(1000);
        System.exit(0);
    }
}