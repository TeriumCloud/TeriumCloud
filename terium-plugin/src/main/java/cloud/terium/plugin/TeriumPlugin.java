package cloud.terium.plugin;

import cloud.terium.plugin.impl.event.EventProvider;
import cloud.terium.plugin.impl.pipe.TeriumNetworkListener;
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
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import com.velocitypowered.api.proxy.Player;
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
    // Template
    private final TemplateFactory templateFactory;
    private final TemplateProvider templateProvider;
    // Network
    private final TeriumNetworkListener teriumNetworkListener;
    // Utils
    private String thisName;

    public TeriumPlugin() {
        super();
        instance = this;
        this.serviceFactory = new ServiceFactory();
        this.serviceProvider = new ServiceProvider();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.templateFactory = new TemplateFactory();
        this.templateProvider = new TemplateProvider();
        this.teriumNetworkListener = new TeriumNetworkListener();
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
                return null;
            }

            @Override
            public IEventProvider getEventProvider() {
                return new EventProvider();
            }

            @Override
            public IModuleProvider getModuleProvider() {
                return null;
            }

            @Override
            public INodeProvider getNodeProvider() {
                return null;
            }

            @Override
            public ITemplateProvider getTemplateProvider() {
                return templateProvider;
            }

            @Override
            public IDefaultTeriumNetworking getTeriumNetworking() {
                return teriumNetworkListener;
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
                return null;
            }

            @Override
            public ICommandFactory getCommandFactory() {
                return null;
            }
        };
    }

    public long usedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public @NotNull Optional<ICloudService> getFallback(final Player player) {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllCloudServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.getServiceGroup().getServiceType().equals(ServiceType.Proxy))
                .filter(service -> service.getServiceGroup().getServiceType().equals(ServiceType.Lobby))
                .filter(service -> !service.isLocked())
                .filter(service -> (player.getCurrentServer().isEmpty()
                        || !player.getCurrentServer().get().getServerInfo().getName().equals(service.getServiceName())))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers));
    }
}