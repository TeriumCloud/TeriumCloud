package cloud.terium.plugin;

import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.networking.packet.service.PacketPlayOutUpdateService;
import cloud.terium.plugin.impl.console.CommandFactory;
import cloud.terium.plugin.impl.console.ConsoleProvider;
import cloud.terium.plugin.impl.event.EventProvider;
import cloud.terium.plugin.impl.module.ModuleProvider;
import cloud.terium.plugin.impl.node.NodeFactory;
import cloud.terium.plugin.impl.node.NodeProvider;
import cloud.terium.plugin.impl.pipe.DefaultTeriumNetworking;
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
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.module.IModuleProvider;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.node.INodeFactory;
import cloud.terium.teriumapi.node.INodeProvider;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import cloud.terium.teriumapi.service.*;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public final class TeriumPlugin extends TeriumAPI {

    private static TeriumPlugin instance;
    // Service
    private final ServiceFactory serviceFactory;
    private final ServiceProvider serviceProvider;
    // Service group
    private final ServiceGroupFactory serviceGroupFactory;
    private final ServiceGroupProvider serviceGroupProvider;
    // Node
    private final NodeFactory nodeFactory;
    private final NodeProvider nodeProvider;
    // Template
    private final TemplateFactory templateFactory;
    private final TemplateProvider templateProvider;
    // Console
    private final ConsoleProvider consoleProvider;
    private final CommandFactory commandFactory;
    // Network
    private final DefaultTeriumNetworking defaultTeriumNetworking;
    // Event
    private final EventProvider eventProvider;
    // Module
    private final ModuleProvider moduleProvider;
    // Utils
    private final String thisName;

    public TeriumPlugin() {
        super();
        instance = this;
        this.defaultTeriumNetworking = new DefaultTeriumNetworking();
        this.serviceFactory = new ServiceFactory();
        this.serviceProvider = new ServiceProvider();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.nodeFactory = new NodeFactory();
        this.nodeProvider = new NodeProvider();
        this.templateFactory = new TemplateFactory();
        this.templateProvider = new TemplateProvider();
        this.consoleProvider = new ConsoleProvider();
        this.commandFactory = new CommandFactory();
        this.eventProvider = new EventProvider();
        this.moduleProvider = new ModuleProvider();

        thisName = System.getProperty("servicename");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                defaultTeriumNetworking.sendPacket(new PacketPlayOutSuccessfullyServiceStarted(new ICloudService() {
                    @Override
                    public String getServiceName() {
                        return "test-1";
                    }

                    @Override
                    public int getServiceId() {
                        return 0;
                    }

                    @Override
                    public int getPort() {
                        return 0;
                    }

                    @Override
                    public List<ITemplate> getTemplates() {
                        return null;
                    }

                    @Override
                    public int getOnlinePlayers() {
                        return 0;
                    }

                    @Override
                    public void setOnlinePlayers(int onlinePlayers) {

                    }

                    @Override
                    public long getUsedMemory() {
                        return 0;
                    }

                    @Override
                    public void setUsedMemory(long usedMemory) {

                    }

                    @Override
                    public void update() {

                    }

                    @Override
                    public ICloudServiceGroup getServiceGroup() {
                        return null;
                    }

                    @Override
                    public INode getServiceNode() {
                        return null;
                    }

                    @Override
                    public ServiceState getServiceState() {
                        return null;
                    }

                    @Override
                    public void setServiceState(ServiceState serviceState) {

                    }

                    @Override
                    public boolean isLocked() {
                        return false;
                    }

                    @Override
                    public void setLocked(boolean locked) {

                    }

                    @Override
                    public void addProperty(String name, Object property) {

                    }

                    @Override
                    public void removeProperty(String name) {

                    }

                    @Override
                    public Object getProperty(String name) {
                        return null;
                    }
                }));
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
                return getServiceProvider().getCloudServiceByName(thisName).orElseGet(null);
            }

            @Override
            public INode getThisNode() {
                return getThisService().getServiceGroup().getGroupNode();
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
                return null;
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
                return defaultTeriumNetworking;
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
                return commandFactory;
            }
        };
    }

    public long usedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public @NotNull Optional<ICloudService> getFallback(final ICloudPlayer player) {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllCloudServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.getServiceGroup().getServiceType().equals(ServiceType.Proxy))
                .filter(service -> service.getServiceGroup().getServiceType().equals(ServiceType.Lobby))
                .filter(service -> !service.isLocked())
                .filter(service -> (player.getConnectedCloudService().isEmpty()
                        || !player.getConnectedCloudService().get().getServiceName().equals(service.getServiceName())))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers));
    }
}