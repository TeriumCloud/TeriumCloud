package cloud.terium.cloudsystem.node.service;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.common.utils.version.ServerVersions;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.networking.packet.service.PacketPlayOutServiceAdd;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRemove;
import cloud.terium.networking.packet.service.PacketPlayOutUpdateService;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
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

public class CloudService implements ICloudService {

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
        this(cloudServiceGroup.getTemplates(), cloudServiceGroup, NodeStartup.getNode().getServiceProvider().getFreeServiceId(cloudServiceGroup), cloudServiceGroup.hasPort() ? cloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    public CloudService(ICloudServiceGroup cloudServiceGroup, List<ITemplate> templates) {
        this(templates, cloudServiceGroup, NodeStartup.getNode().getServiceProvider().getFreeServiceId(cloudServiceGroup), cloudServiceGroup.hasPort() ? cloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), cloudServiceGroup.getMaxPlayers());
    }

    public CloudService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port) {
        this(templates, cloudServiceGroup, serviceId != -1 ? serviceId : NodeStartup.getNode().getServiceProvider().getFreeServiceId(cloudServiceGroup), port, cloudServiceGroup.getMaxPlayers());
    }

    public CloudService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port, int maxPlayers) {
        this(cloudServiceGroup.getGroupName(), templates, cloudServiceGroup, cloudServiceGroup.getServiceType(), serviceId, port, maxPlayers, cloudServiceGroup.getMemory());
    }

    public CloudService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port, int maxPlayers, int memory) {
        this(cloudServiceGroup.getGroupName(), templates, cloudServiceGroup, cloudServiceGroup.getServiceType(), serviceId, port, maxPlayers, memory);
    }

    public CloudService(String serviceName, List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, ServiceType serviceType, int serviceId, int port, int maxPlayers, int maxMemory) {
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
        templates.addAll(serviceGroup.getTemplates());
        NodeStartup.getNode().getScreenProvider().addCloudService(this);
        NodeStartup.getNode().getServiceProvider().addService(this);
        NodeStartup.getNode().getServiceProvider().putServiceId(cloudServiceGroup, serviceId);
        if (NodeStartup.getNode().isDebugMode())
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
        NodeStartup.getNode().getModuleProvider().getAllModules().stream().filter(module -> module.getModuleType() == ModuleType.valueOf(getServiceType().name())).forEach(module -> {
            try {
                FileUtils.copyFileToDirectory(new File("modules//" + module.getFileName()), new File("servers//" + getServiceName() + "//plugins//"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        AtomicBoolean hasJarFile = new AtomicBoolean(false);
        Arrays.stream(folder.listFiles()).forEach(file -> {
            if (file.getName().contains(".jar"))
                hasJarFile.set(true);
        });
        if (!hasJarFile.get()) {
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
            properties.setProperty("server-ip", NodeStartup.getNode().getNodeConfig().serviceAddress());
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

        NodeStartup.getNode().getNetworking().sendPacket(new PacketPlayOutServiceAdd(getServiceName(), serviceId, port, maxPlayers, getMaxMemory(),
                getServiceNode().getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), propertyMap));

        this.thread = new Thread(() -> {
            String[] command = new String[]{"java", "-jar", "-Xmx" + serviceGroup.getMemory() + "m", "-Dservicename=" + getServiceName(), "-Dservicenode=" + getServiceNode().getName(), "-Dnetty-address=" + NodeStartup.getNode().getNodeConfig().master().get("ip").getAsString(), "-Dnetty-port=" + NodeStartup.getNode().getNodeConfig().master().get("port").getAsInt(), serviceGroup.getVersion() + ".jar", "nogui"};
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
            } catch (InterruptedException ignored) {
            }

            if (TeriumCloud.getTerium().getCloudUtils().isInScreen() && NodeStartup.getNode().getScreenProvider().getCurrentScreen().equals(this))
                toggleScreen();
            NodeStartup.getNode().getScreenProvider().removeCloudService(this);
            NodeStartup.getNode().getNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));
            delete();
            Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
        });
        this.thread.start();
    }

    @Override
    public void forceShutdown() {
        shutdown();
    }

    public void shutdown() {
        if (NodeStartup.getNode().isDebugMode())
            Logger.log("Trying to stop service '" + getServiceName() + "'... [CloudService#shutdown]", LogType.INFO);
        if (process != null)
            process.destroyForcibly();
        thread.interrupt();
    }

    @SneakyThrows
    public void delete() {
        NodeStartup.getNode().getServiceProvider().removeServiceId(serviceGroup, serviceId);
        FileUtils.deleteDirectory(folder);
        NodeStartup.getNode().getServiceProvider().removeService(this);
    }

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
                    NodeStartup.getNode().getScreenProvider().addLogToScreen(this, "[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.SCREEN.getPrefix() + line);
                }
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            if (NodeStartup.getNode().getScreenProvider().getLogsFromService(this) != null) {
                NodeStartup.getNode().getScreenProvider().getLogsFromService(this).forEach(log -> Logger.log(log, LogType.SCREEN));
            }
            Logger.log("You're now inside of " + getServiceName() + ".", LogType.INFO);
            TeriumCloud.getTerium().getCloudUtils().setInScreen(true);
            NodeStartup.getNode().getScreenProvider().setCurrentScreen(this);
            outputThread.start();
        } else {
            outputThread.stop();
            NodeStartup.getNode().getScreenProvider().setCurrentScreen(null);
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
        NodeStartup.getNode().getNetworking().sendPacket(new PacketPlayOutUpdateService(getServiceName(), getOnlinePlayers(), getUsedMemory(), getServiceState(), isLocked(), getPropertyMap()));
    }

    @Override
    public ICloudServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    @Override
    public INode getServiceNode() {
        return NodeStartup.getNode().getThisNode();
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

    @Override
    public HashMap<String, Object> getPropertyMap() {
        return propertyMap;
    }
}