package cloud.terium.bridge;

import cloud.terium.bridge.impl.config.ConfigManager;
import cloud.terium.bridge.impl.networking.DefaultTeriumNetworking;
import cloud.terium.bridge.impl.service.ServiceManager;
import cloud.terium.bridge.impl.service.group.ServiceGroupManager;
import cloud.terium.bridge.networking.TeriumNetworkListener;
import cloud.terium.bridge.player.CloudPlayerManager;
import cloud.terium.bridge.player.CloudRank;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.networking.json.DefaultJsonService;
import cloud.terium.networking.packets.PacketPlayOutServiceOnline;
import cloud.terium.teriumapi.service.CloudServiceState;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.impl.CloudService;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class TeriumBridge {

    private static TeriumBridge instance;
    private final ServiceManager serviceManager;
    private final ServiceGroupManager serviceGroupManager;
    private final TeriumNetworkListener teriumNetworkListener;
    private final CloudPlayerManager cloudPlayerManager;
    private ConfigManager configManager;
    private String thisName;
    private final String prefix;

    public static void main(String[] args) {
        new TeriumBridge();
    }

    /*
     * FINISHED: Init implementation for ICloudService or something else to set the ServiceState
     *  TODO: Test this implementation
     */

    public TeriumBridge() {
        instance = this;
        this.prefix = "<gradient:#245dec:#00d4ff>Terium</gradient> <dark_gray>⇨ <white>";
        this.serviceManager = new ServiceManager();
        this.serviceGroupManager = new ServiceGroupManager();
        this.configManager = new ConfigManager();
        this.teriumNetworkListener = new TeriumNetworkListener(new DefaultTeriumNetworking(configManager));
        this.cloudPlayerManager = new CloudPlayerManager();
    }

    public static TeriumBridge getInstance() {
        return instance;
    }

    public ICloudService getThisService() {
        return serviceManager.getCloudServiceByName(thisName);
    }

    public void reloadCloudBridge() {
        this.configManager = new ConfigManager();

        if (getThisService().getServiceGroup().getServiceType().equals(CloudServiceType.Proxy)) {
            BridgeVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> cloudPlayerManager.getCloudPlayer(player.getUsername(), player.getUniqueId()).hasRankOrHigher(CloudRank.Admin)).forEach(player ->
                    player.sendMessage(MiniMessage.miniMessage().deserialize(prefix + "Successfully reloaded cloudbridge-velocity.")));
        }
    }

    public long usedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public long maxMemory() {
        return (Runtime.getRuntime().maxMemory()) / (1024 * 1024);
    }

    @SneakyThrows
    public void initServices(ProxyServer server) {
        thisName = new Toml().read(new File("velocity.toml")).getString("name");
        for (File file : new File("../../data/cache/servers").listFiles()) {
            DefaultJsonService jsonService = new DefaultJsonService(file.getName().replace(".json", ""));

            if (serviceManager.getCloudServiceByName(jsonService.getString("service_name")) == null) {
                serviceManager.addService(new CloudService(jsonService.getString("service_name"), jsonService.getInt("serviceid"), jsonService.getInt("port"), serviceGroupManager.getServiceGroupByName(jsonService.getString("service_group"))));

                if (!jsonService.getString("service_group").equals("Proxy"))
                    server.registerServer(new ServerInfo(jsonService.getString("service_name"), new InetSocketAddress("127.0.0.1", jsonService.getInt("port"))));
            } else {
                System.out.println("This service is already registed! (" + jsonService.getString("service_name") + ")");
            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getThisService().setServiceState(CloudServiceState.ONLINE);
                TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceOnline(getThisService().getServiceName(), true));
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

            if (serviceManager.getCloudServiceByName(jsonService.getString("service_name")) == null) {
                serviceManager.addService(new CloudService(jsonService.getString("service_name"), jsonService.getInt("serviceid"), jsonService.getInt("port"), serviceGroupManager.getServiceGroupByName(jsonService.getString("service_group"))));
            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getThisService().setServiceState(CloudServiceState.ONLINE);
                TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceOnline(getThisService().getServiceName(), true));
            }
        }, 500);
    }
}