package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.event.events.service.ServiceAddEvent;
import cloud.terium.cloudsystem.event.events.service.ServiceUpdateEvent;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.version.ServerVersions;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRemove;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class CloudService extends cloud.terium.teriumapi.service.impl.CloudService {

    private final ICloudServiceGroup serviceGroup;
    private final String name;
    private final File folder;
    private final int port;
    private final int serviceId;
    private final int maxPlayers;
    private final int maxMemory;
    private final List<ITemplate> templates;
    private final HashMap<String, Object> propertyMap;
    private ServiceState serviceState;
    private ServiceType serviceType;
    private long usedMemory;
    private int onlinePlayers;
    private boolean locked;
    private Process process;
    private Thread outputThread;
    private Thread thread;

    public CloudService(ICloudServiceGroup cloudServiceGroup) {
        this(cloudServiceGroup.getTemplates(), cloudServiceGroup, TeriumCloud.getTerium().getServiceProvider().getFreeServiceId(cloudServiceGroup), cloudServiceGroup.hasPort() ? cloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    public CloudService(ICloudServiceGroup cloudServiceGroup, List<ITemplate> templates) {
        this(templates, cloudServiceGroup, TeriumCloud.getTerium().getServiceProvider().getFreeServiceId(cloudServiceGroup), cloudServiceGroup.hasPort() ? cloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), cloudServiceGroup.getMaxPlayers());
    }

    public CloudService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port) {
        this(templates, cloudServiceGroup, serviceId != -1 ? serviceId : TeriumCloud.getTerium().getServiceProvider().getFreeServiceId(cloudServiceGroup), port, cloudServiceGroup.getMaxPlayers());
    }

    public CloudService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port, int maxPlayers) {
        this(cloudServiceGroup.getGroupName(), templates, cloudServiceGroup, cloudServiceGroup.getServiceType(), serviceId, port, maxPlayers, cloudServiceGroup.getMemory());
    }

    public CloudService(String serviceName, List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, ServiceType serviceType, int serviceId, int port, int maxPlayers, int maxMemory) {
        super(serviceName, serviceId, port, cloudServiceGroup.getGroupNode(), cloudServiceGroup, templates);
        this.serviceGroup = cloudServiceGroup;
        this.serviceId = serviceId;
        this.name = serviceName;
        this.serviceType = serviceType;
        this.serviceState = ServiceState.PREPARING;
        this.templates = templates;
        this.folder = new File("servers//" + getServiceName());
        this.propertyMap = new HashMap<>();
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.maxMemory = maxMemory;
        this.usedMemory = 0;
        this.onlinePlayers = 0;
        TeriumCloud.getTerium().getScreenProvider().addCloudService(this);
        TeriumCloud.getTerium().getServiceProvider().addService(this);
        Logger.log("Successfully created service " + getServiceName() + ".", LogType.INFO);
    }

    @SneakyThrows
    public void start() {
        this.folder.mkdirs();
        FileUtils.copyFileToDirectory(new File(serviceGroup.getServiceType() == ServiceType.Lobby || serviceGroup.getServiceType() == ServiceType.Server ? "data//versions//spigot.yml" : "data//versions//velocity.toml"), folder);
        FileUtils.copyDirectory(new File(serviceGroup.getServiceType() == ServiceType.Lobby || serviceGroup.getServiceType() == ServiceType.Server ? "templates//Global//server" : "templates//Global//proxy"), folder);
        FileUtils.copyFileToDirectory(new File("data//versions//teriumcloud-plugin.jar"), new File("servers//" + getServiceName() + "//plugins//"));
        templates.forEach(template -> {
            try {
                FileUtils.copyDirectory(template.getPath().toFile(), folder);
            } catch (IOException ignored) {
            }
        });

        AtomicBoolean hasJarFile = new AtomicBoolean(false);
        Arrays.stream(folder.listFiles()).forEach(file -> {
            if(file.getName().contains(".jar"))
                hasJarFile.set(true);
        });
        if(!hasJarFile.get()) {
            try {
                FileUtils.copyURLToFile(new URL(ServerVersions.valueOf(serviceGroup.getVersion().toUpperCase().replace(".", "_").replace("-", "_")).getUrl()), new File("servers//" + getServiceName() + "//" + serviceGroup.getVersion() + ".jar"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if (serviceGroup.getServiceType() == ServiceType.Lobby || serviceGroup.getServiceType() == ServiceType.Server) {
            Logger.log("The service '" + getServiceName() + "' is starting on port " + port + ".", LogType.INFO);

            Properties properties = new Properties();
            File serverProperties = new File(this.folder, "server.properties");
            properties.setProperty("server-name", getServiceName());
            properties.setProperty("server-port", getPort() + "");
            properties.setProperty("server-ip", "127.0.0.1");
            properties.setProperty("online-mode", "false");

            try (OutputStream outputStream = new FileOutputStream(serverProperties);
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

                properties.store(writer, null);
            }

            properties = new Properties();
            File eula = new File(this.folder, "eula.txt");

            eula.createNewFile();
            properties.setProperty("eula", "true");

            try (OutputStream outputStream = new FileOutputStream(eula)) {
                properties.store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), "Auto eula agreement by TeriumCloud.");
            }
        } else {
            Logger.log("The service '" + getServiceName() + "' is starting on port " + port + ".", LogType.INFO);
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%name%", getServiceName());
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%port%", port + "");
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%max_players%", serviceGroup.getMaxPlayers() + "");
        }

        if (!serviceGroup.getServiceType().equals(ServiceType.Proxy))
            TeriumCloud.getTerium().getEventProvider().callEvent(new ServiceAddEvent(this));

        this.thread = new Thread(() -> {
            String[] command = new String[]{"java", "-jar", "-Xmx" + serviceGroup.getMemory() + "m", "-Dservicename=" + getServiceName(), "-Dnetty-address=" + TeriumCloud.getTerium().getCloudConfig().ip(), "-Dnetty-port=" + TeriumCloud.getTerium().getCloudConfig().port(), serviceGroup.getVersion() + ".jar", "nogui"};
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            processBuilder.directory(this.folder);
            try {
                this.process = processBuilder.start();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            int resultCode = 0;
            try {
                resultCode = this.process.waitFor();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            shutdown();
            delete();
        });
        this.thread.start();
    }

    public void shutdown() {
        if (TeriumCloud.getTerium().getCloudUtils().isInScreen() && TeriumCloud.getTerium().getScreenProvider().getCurrentScreen().equals(this))
            toggleScreen();
        TeriumCloud.getTerium().getScreenProvider().removeCloudService(this);
        Logger.log("Trying to stop service '" + getServiceName() + "'... [CloudService#shutdown]", LogType.INFO);
        if (!serviceGroup.getServiceType().equals(ServiceType.Proxy))
            TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceRemove(this));

        process.destroyForcibly().onExit().thenRun(() -> {
            thread.stop();
            delete();
        });
        Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
    }

    public void restart() {
        if (TeriumCloud.getTerium().getCloudUtils().isInScreen() && TeriumCloud.getTerium().getScreenProvider().getCurrentScreen().equals(this))
            toggleScreen();
        Logger.log("Trying to stop service '" + getServiceName() + "'... [CloudService#restart  ]", LogType.INFO);
        if (!serviceGroup.getServiceType().equals(ServiceType.Proxy))
            TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutServiceRemove(this));

        setOnlinePlayers(0);
        setUsedMemory(0);
        setServiceState(ServiceState.PREPARING);
        process.destroy();
        thread.stop();
        update();
        Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
        start();
    }

    @SneakyThrows
    public void delete() {
        FileUtils.deleteDirectory(folder);
        TeriumCloud.getTerium().getServiceProvider().removeService(this);
    }

    /*@SneakyThrows
    @Override
    public void forceShutdown() {
        MinecraftService minecraftService = this;
        Logger.log("Trying to stop service '" + getServiceName() + "'. [MinecraftService#forceShutdown]", LogType.INFO);
        if (!serviceGroup.getServiceType().equals(ServiceType.Proxy))
            TeriumCloud.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));
        thread.stop();
        process.destroyForcibly();
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                FileUtils.deleteDirectory(folder);
                TeriumCloud.getTerium().getServiceManager().removeService(minecraftService);
                Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
            }
        }, 5000);
    }*/

    public void toggleScreen() {
        if (!TeriumCloud.getTerium().getCloudUtils().isInScreen()) {
            outputThread = new Thread(() -> {
                String line = null;
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while (true) {
                    try {
                        if ((line = input.readLine()) == null) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Logger.log(line, LogType.SCREEN);
                    TeriumCloud.getTerium().getScreenProvider().addLogToScreen(this, "[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.SCREEN.getPrefix() + line);
                }
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            if (TeriumCloud.getTerium().getScreenProvider().getLogsFromService(this) != null) {
                TeriumCloud.getTerium().getScreenProvider().getLogsFromService(this).forEach(log -> Logger.log(log, LogType.SCREEN));
            }
            Logger.log("You're now inside of " + getServiceName() + ".", LogType.INFO);
            TeriumCloud.getTerium().getCloudUtils().setInScreen(true);
            TeriumCloud.getTerium().getScreenProvider().setCurrentScreen(this);
            outputThread.start();
        } else {
            outputThread.stop();
            TeriumCloud.getTerium().getScreenProvider().setCurrentScreen(null);
            TeriumCloud.getTerium().getCloudUtils().setInScreen(false);
            Logger.log("You left the screen from " + getServiceName() + ".", LogType.INFO);
            Logger.logAllCachedLogs();
        }
    }

    private void replaceInFile(File file, String placeHolder, String replacedWith) {
        String content;
        try {
            final Path path = file.toPath();

            content = Files.readString(path);
            content = content.replace(placeHolder, replacedWith);

            Files.writeString(path, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServiceName() {
        return getServiceId() > 9 ? name + "-" + getServiceId() : name + "-0" + getServiceId();
    }

    @Override
    public int getServiceId() {
        return serviceId;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public List<ITemplate> getTemplates() {
        return templates;
    }

    @Override
    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    @Override
    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    @Override
    public long getUsedMemory() {
        return usedMemory;
    }

    @Override
    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    @Override
    public void update() {
        TeriumCloud.getTerium().getEventProvider().callEvent(new ServiceUpdateEvent(this));
    }

    @Override
    public ICloudServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    @Override
    public INode getServiceNode() {
        return TeriumCloud.getTerium().getThisNode();
    }

    @Override
    public ServiceState getServiceState() {
        return serviceState;
    }

    @Override
    public void setServiceState(ServiceState serviceState) {
        this.serviceState = serviceState;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void addProperty(String name, Object property) {
        this.propertyMap.put(name, property);
    }

    @Override
    public void removeProperty(String name) {
        this.propertyMap.remove(name);
    }

    @Override
    public Object getProperty(String name) {
        return propertyMap.get(name);
    }
}