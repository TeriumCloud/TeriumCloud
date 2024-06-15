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
import cloud.terium.cloudsystem.node.node.NodeProvider;
import cloud.terium.cloudsystem.node.pipe.TeriumNetworkProvider;
import cloud.terium.cloudsystem.node.service.CloudServiceFactory;
import cloud.terium.cloudsystem.node.service.CloudServiceListener;
import cloud.terium.cloudsystem.node.service.CloudServiceProvider;
import cloud.terium.cloudsystem.node.service.group.ServiceGroupFactory;
import cloud.terium.cloudsystem.node.service.group.ServiceGroupProvider;
import cloud.terium.cloudsystem.node.template.TemplateFactory;
import cloud.terium.cloudsystem.node.template.TemplateListener;
import cloud.terium.cloudsystem.node.template.TemplateProvider;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.networking.packet.node.PacketPlayOutNodeShutdowned;
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
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

@Getter
@Setter
public class NodeStartup extends TeriumAPI {

    private static NodeStartup node;
    private final String ipAddress;
    private final CommandManager commandManager;
    private final ConsoleManager consoleManager;
    private final TeriumNetworkProvider networking;
    private final NodeProvider nodeProvider;
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
    private NodeConfig nodeConfig;
    private ConfigManager configManager;

    @SneakyThrows
    public NodeStartup() {
        node = this;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            ipAddress = socket.getLocalAddress().getHostAddress();
        }

        TeriumCloud.getTerium().getCloudUtils();
        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b__   _  _____  ______  _______
                 §f  |    |______ |_____/   |   |     | |  |  | §b| \\  | |     | |     \\ |______
                 §f  |    |______ |    \\_ __|__ |_____| |  |  | §b|  \\_| |_____| |_____/ |______ §7[§f%version%§7]
                                                                                                 \s
                 §7> §fTerium by ByRaudy(Jannik H.) §7& §fveteex(Niklas S.)\s
                 §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                 """.replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion()));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "Welcome to terium-cloud!"));
        Logger.log(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.INFO.getPrefix() + "terium-cloud is starting phase §6one §7of the startup... Please wait a small while."));

        this.configManager = new ConfigManager();
        this.nodeConfig = configManager.toNodeConfig();
        this.networking = new TeriumNetworkProvider();
        this.thisNode = new Node(nodeConfig.name(), new InetSocketAddress(ipAddress, nodeConfig.port()), nodeConfig.memory(), true);

        this.eventProvider = new EventProvider();
        this.eventProvider.subscribeListener(new ConsoleListener());
        this.eventProvider.subscribeListener(new CloudPlayerListener());
        this.eventProvider.subscribeListener(new CloudServiceListener());
        this.eventProvider.subscribeListener(new TemplateListener());

        this.templateProvider = new TemplateProvider();
        this.templateFactory = new TemplateFactory();
        this.nodeProvider = new NodeProvider();
        this.nodeProvider.getAllNodes().add(thisNode);
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.serviceProvider = new CloudServiceProvider();
        this.serviceFactory = new CloudServiceFactory();
        this.cloudPlayerProvider = new CloudPlayerProvider();
        this.screenProvider = new ScreenProvider();
        this.moduleProvider = new ModuleProvider();
        this.commandManager = new CommandManager();
        this.consoleManager = new ConsoleManager(commandManager);

        if (nodeConfig.checkUpdate()) {
            Logger.log("Trying to download 'teriumcloud-plugin.jar'...");
            try {
                FileUtils.copyURLToFile(new URL("https://terium.cloud/utils/teriumcloud-plugin.jar"), new File("data//versions//teriumcloud-plugin.jar"));
                Logger.log("Successfully to downloaded 'teriumcloud-plugin.jar'.");
            } catch (Exception exception) {
                Logger.log("Download of latest teriumcloud-plugin.jar is failed.");
            }
        }

        Logger.log("Starting phase §6two §fof the startup...", LogType.INFO);
        Thread.sleep(2000);

        consoleManager.clearScreen();
        Logger.clearAllLoggedMessags();
        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b__   _  _____  ______  _______
                 §f  |    |______ |_____/   |   |     | |  |  | §b| \\  | |     | |     \\ |______
                 §f  |    |______ |    \\_ __|__ |_____| |  |  | §b|  \\_| |_____| |_____/ |______ §7[§f%version%§7]
                                                                                                 \s
                §7> §fTerium by ByRaudy(Jannik H.) §7& §fveteex(Niklas S.)\s
                §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                                 
                 §a> §fConnected with terium-server on %ip%:%port%.
                 §a> §fRecived %commands% commands successfully.
                 §a> §fRecived %groups% groups successfully.
                 §a> §fLoaded %templates% templates successfully.
                                 
                 """.replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion()).replace("%templates%", templateProvider.getAllTemplates().size() + "").replace("%commands%", commandManager.getBuildedCommands().keySet().size() + "")
                .replace("%ip%", nodeConfig.master().get("ip").getAsString()).replace("%port%", nodeConfig.master().get("port").getAsInt() + "").replace("%groups%", serviceGroupProvider.getAllServiceGroups().size() + ""));
        this.moduleProvider.loadModules();
        this.networking.sendPacket(new PacketPlayOutNodeStarted(thisNode.getName(), thisNode.getAddress(), thisNode.getMaxMemory(), nodeConfig.master().get("key").getAsString()));

        Signal.handle(new Signal("INT"), signal -> {
            TeriumCloud.getTerium().getCloudUtils().setRunning(false);
            shutdownCloud();
        });

        serviceProvider.startServiceCheck();
        serviceProvider.startServiceStopCheck();
        NodeStartup.getNode().getServiceGroupProvider().getAllServiceGroups().forEach(cloudServiceGroup -> getServiceProvider().getCloudServiceIdCache().put(cloudServiceGroup, new LinkedList<>()));
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
            public ICommandFactory getCommandFactory() {
                return commandManager;
            }
        };
    }

    @SneakyThrows
    public void shutdownCloud() {
        Logger.log("Trying to stop terium-cloud...", LogType.INFO);

        TeriumCloud.getTerium().getCloudUtils().setRunning(false);
        getServiceProvider().getAllServices().stream().filter(cloudService -> cloudService.getServiceNode().getName().equals(thisNode.getName())).forEach(ICloudService::forceShutdown);
        Thread.sleep(500);
        Logger.log("Successfully stopped all services.", LogType.INFO);
        Thread.sleep(1000);
        getNetworking().sendPacket(new PacketPlayOutNodeShutdowned(thisNode.getName()));
        getNetworking().getChannel().close().sync();
        Logger.log("Successfully disconnected from terium-server.", LogType.INFO);

        FileUtils.deleteDirectory(new File("servers//"));
        Logger.log("Successfully deleted server folder.", LogType.INFO);
        Thread.sleep(300);
        Logger.log("Successfully stopped terium-cloud. Goodbye!", LogType.INFO);
        Thread.sleep(1000);
        System.exit(0);
    }
}