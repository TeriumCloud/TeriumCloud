package cloud.terium.plugin;

import cloud.terium.networking.packet.PacketPlayOutCheckVersion;
import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.plugin.impl.config.ConfigManager;
import cloud.terium.plugin.impl.console.CommandFactory;
import cloud.terium.plugin.impl.console.ConsoleProvider;
import cloud.terium.plugin.impl.entity.CloudPlayerProvider;
import cloud.terium.plugin.impl.event.EventProvider;
import cloud.terium.plugin.impl.module.ModuleProvider;
import cloud.terium.plugin.impl.node.NodeProvider;
import cloud.terium.plugin.impl.pipe.TeriumNetworking;
import cloud.terium.plugin.impl.service.ServiceFactory;
import cloud.terium.plugin.impl.service.ServiceProvider;
import cloud.terium.plugin.impl.service.group.ServiceGroupFactory;
import cloud.terium.plugin.impl.service.group.ServiceGroupProvider;
import cloud.terium.plugin.impl.template.TemplateFactory;
import cloud.terium.plugin.impl.template.TemplateProvider;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.api.ICloudFactory;
import cloud.terium.teriumapi.api.ICloudProvider;
import cloud.terium.teriumapi.console.IConsoleProvider;
import cloud.terium.teriumapi.console.command.ICommandFactory;
import cloud.terium.teriumapi.entity.ICloudPlayerProvider;
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.module.IModuleProvider;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeProvider;
import cloud.terium.teriumapi.pipe.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.service.*;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public final class TeriumPlugin extends TeriumAPI {

    private static TeriumPlugin instance;
    private final ConfigManager configManager;
    // Service
    private final ServiceFactory serviceFactory;
    private final ServiceProvider serviceProvider;
    // Service group
    private final ServiceGroupFactory serviceGroupFactory;
    private final ServiceGroupProvider serviceGroupProvider;
    // Node
    private final NodeProvider nodeProvider;
    // Template
    private final TemplateFactory templateFactory;
    private final TemplateProvider templateProvider;
    // Console
    private final ConsoleProvider consoleProvider;
    private final CommandFactory commandFactory;
    // Player
    private final CloudPlayerProvider cloudPlayerProvider;
    // Network
    private final TeriumNetworking teriumNetworking;
    // Event
    private final EventProvider eventProvider;
    // Module
    private final ModuleProvider moduleProvider;
    // Utils
    private final String thisName;

    public TeriumPlugin() {
        super();
        instance = this;
        this.configManager = new ConfigManager();
        this.teriumNetworking = new TeriumNetworking();
        this.serviceFactory = new ServiceFactory();
        this.serviceProvider = new ServiceProvider();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.nodeProvider = new NodeProvider();
        this.templateFactory = new TemplateFactory();
        this.templateProvider = new TemplateProvider();
        this.consoleProvider = new ConsoleProvider();
        this.commandFactory = new CommandFactory();
        this.cloudPlayerProvider = new CloudPlayerProvider();
        this.eventProvider = new EventProvider();
        this.moduleProvider = new ModuleProvider();

        thisName = System.getProperty("servicename");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                teriumNetworking.sendPacket(new PacketPlayOutSuccessfullyServiceStarted(thisName, System.getProperty("servicenode")));
                teriumNetworking.sendPacket(new PacketPlayOutCheckVersion(getProvider().getVersion()));
                getTeriumAPI().getProvider().getThisService().setServiceState(ServiceState.ONLINE);
                getTeriumAPI().getProvider().getThisService().update();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getProvider().getThisService().setUsedMemory(usedMemory());
                        getProvider().getThisService().update();
                    }
                }, 0, 2000);
            }
        }, 1500);
    }

    public static TeriumPlugin getInstance() {
        return instance;
    }

    @Override
    public ICloudProvider getProvider() {
        return new ICloudProvider() {
            @Override
            public ICloudService getThisService() {
                return getServiceProvider().getServiceByName(thisName).orElseGet(null);
            }

            @Override
            public INode getThisNode() {
                return getNodeProvider().getNodeByName(System.getProperty("servicenode")).orElseGet(null);
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
                return consoleProvider;
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
                return teriumNetworking;
            }

            @Override
            public String getVersion() {
                return "1.8-OXYGEN";
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
                return commandFactory;
            }
        };
    }

    public long usedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public @NotNull Optional<ICloudService> getFallback(final Player player) {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.getServiceGroup().getServiceType().equals(ServiceType.Proxy))
                .filter(service -> service.getServiceGroup().getServiceType().equals(ServiceType.Lobby))
                .filter(service -> !service.isLocked())
                .filter(service -> (player.getCurrentServer().isEmpty()
                        || !player.getCurrentServer().get().getServerInfo().getName().equals(service.getServiceName())))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers));
    }

    public @NotNull Optional<ICloudService> getWaterfallFallback(final ProxiedPlayer player) {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.getServiceGroup().getServiceType().equals(ServiceType.Proxy))
                .filter(service -> service.getServiceGroup().getServiceType().equals(ServiceType.Lobby))
                .filter(service -> !service.isLocked())
                /*.filter(service -> (player.getServer() != null)
                        && !player.getServer().getInfo().getName().equals(service.getServiceName()))*/
                .min(Comparator.comparing(ICloudService::getOnlinePlayers));
    }

    public String getPrefix() {
        return configManager.getJson().get("prefix").getAsString();
    }
}