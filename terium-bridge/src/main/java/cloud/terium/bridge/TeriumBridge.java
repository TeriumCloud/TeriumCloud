package cloud.terium.bridge;

import cloud.terium.bridge.impl.ConsoleProvider;
import cloud.terium.bridge.impl.config.ConfigManager;
import cloud.terium.bridge.impl.networking.DefaultTeriumNetworking;
import cloud.terium.bridge.impl.service.ServiceFactory;
import cloud.terium.bridge.impl.service.ServiceProvider;
import cloud.terium.bridge.impl.service.group.ServiceGroupFactory;
import cloud.terium.bridge.impl.service.group.ServiceGroupProvider;
import cloud.terium.bridge.impl.template.TemplateFactory;
import cloud.terium.bridge.impl.template.TemplateProvider;
import cloud.terium.bridge.networking.TeriumNetworkListener;
import cloud.terium.bridge.player.CloudPlayerProvider;
import cloud.terium.networking.json.DefaultJsonService;
import cloud.terium.networking.packets.service.PacketPlayOutServiceChangeState;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.api.ICloudFactory;
import cloud.terium.teriumapi.api.ICloudProvider;
import cloud.terium.teriumapi.console.ICloudConsoleProvider;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import cloud.terium.teriumapi.service.*;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupProvider;
import cloud.terium.teriumapi.service.impl.CloudService;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.ITemplateProvider;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.util.*;

@Getter
public class TeriumBridge extends TeriumAPI {

    private static TeriumBridge instance;
    private final TeriumNetworkListener teriumNetworkListener;
    private final ServiceProvider serviceProvider;
    private final ConsoleProvider consoleProvider;
    private final CloudPlayerProvider cloudPlayerProvider;
    private final TemplateProvider templateProvider;
    private final ServiceGroupProvider serviceGroupProvider;
    private final ServiceFactory serviceFactory;
    private final ServiceGroupFactory serviceGroupFactory;
    private final TemplateFactory templateFactory;
    private ConfigManager configManager;
    private String thisName;
    private final List<ICloudPlayer> playerList;
    public TeriumBridge() {
        super();
        instance = this;
        this.templateProvider = new TemplateProvider();
        this.serviceProvider = new ServiceProvider();
        this.serviceGroupProvider = new ServiceGroupProvider();
        this.cloudPlayerProvider = new CloudPlayerProvider();
        this.consoleProvider = new ConsoleProvider();
        this.serviceFactory = new ServiceFactory();
        this.serviceGroupFactory = new ServiceGroupFactory();
        this.templateFactory = new TemplateFactory();
        this.configManager = new ConfigManager();
        this.teriumNetworkListener = new TeriumNetworkListener(new DefaultTeriumNetworking(configManager));
        this.playerList = new ArrayList<>();
    }

    public static TeriumBridge getInstance() {
        return instance;
    }

    @Override
    public ICloudProvider getProvider() {
        return new ICloudProvider() {
            @Override
            public ICloudService getThisService() {
                return getServiceProvider().getCloudServiceByName(thisName);
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
            public ICloudConsoleProvider getConsoleProvider() {
                return consoleProvider;
            }

            @Override
            public ITemplateProvider getTemplateProvider() {
                return templateProvider;
            }

            @Override
            public IDefaultTeriumNetworking getTeriumNetworking() {
                return teriumNetworkListener.getDefaultTeriumNetworking();
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
        };
    }

    public long usedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    @SneakyThrows
    public void initServices(ProxyServer server) {
        thisName = new Toml().read(new File("velocity.toml")).getString("name");
        for (File file : new File("../../data/cache/servers").listFiles()) {
            DefaultJsonService jsonService = new DefaultJsonService(file.getName().replace(".json", ""));

            if (serviceProvider.getCloudServiceByName(jsonService.getString("service_name")) == null) {
                serviceProvider.addService(new CloudService(jsonService.getString("service_name"), jsonService.getInt("serviceid"), jsonService.getInt("port"), serviceGroupProvider.getServiceGroupByName(jsonService.getString("service_group")), templateProvider.getTemplateByName(jsonService.getString("template"))));
                serviceProvider.getCloudServiceByName(jsonService.getString("service_name")).setServiceState(CloudServiceState.valueOf(jsonService.getString("state")));

                if (!jsonService.getString("service_group").equals("Proxy"))
                    server.registerServer(new ServerInfo(jsonService.getString("service_name"), new InetSocketAddress("127.0.0.1", jsonService.getInt("port"))));
            } else {
                System.out.println("This service is already registed! (" + jsonService.getString("service_name") + ")");
            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getProvider().getThisService().setServiceState(CloudServiceState.ONLINE);
                TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceChangeState(getProvider().getThisService().getServiceName(), CloudServiceState.ONLINE));
            }
        }, 500);
    }

    @SneakyThrows
    public void initServices(JavaPlugin plugin) {
        Properties properties = new Properties();
        properties.load(new BufferedReader(new FileReader("server.properties")));
        thisName = properties.getProperty("server-name");
        for (File file : new File("../../data/cache/servers").listFiles()) {
            DefaultJsonService jsonService = new DefaultJsonService(file.getName().replace(".json", ""));

            if (serviceProvider.getCloudServiceByName(jsonService.getString("service_name")) == null) {
                serviceProvider.addService(new CloudService(jsonService.getString("service_name"), jsonService.getInt("serviceid"), jsonService.getInt("port"), serviceGroupProvider.getServiceGroupByName(jsonService.getString("service_group")), templateProvider.getTemplateByName(jsonService.getString("template"))));
            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getProvider().getThisService().setServiceState(CloudServiceState.ONLINE);
                TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceChangeState(getProvider().getThisService().getServiceName(), CloudServiceState.ONLINE));
            }
        }, 500);
    }

    public @NotNull Optional<ICloudService> getFallback(final Player player) {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllCloudServices().stream()
                .filter(service -> service.getServiceState().equals(CloudServiceState.ONLINE))
                .filter(service -> !service.getServiceGroup().getServiceType().equals(CloudServiceType.Proxy))
                .filter(service -> service.getServiceGroup().getServiceType().equals(CloudServiceType.Lobby))
                .filter(service -> !service.isLocked())
                .filter(service -> (player.getCurrentServer().isEmpty()
                        || !player.getCurrentServer().get().getServerInfo().getName().equals(service.getServiceName())))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers));
    }
}