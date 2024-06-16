package cloud.terium.module.dockerizedservices.service;

import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.utils.version.ServerVersions;
import cloud.terium.module.dockerizedservices.TeriumDockerizedServices;
import cloud.terium.networking.packet.service.PacketPlayOutServiceAdd;
import cloud.terium.networking.packet.service.PacketPlayOutServiceExecuteCommand;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRemove;
import cloud.terium.networking.packet.service.PacketPlayOutUpdateService;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class DockerizedService implements ICloudService {

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
    private Thread outputThread;
    private String containerId;

    public DockerizedService(ICloudServiceGroup cloudServiceGroup) {
        this(cloudServiceGroup.getTemplates(), cloudServiceGroup, TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getFreeServiceId(cloudServiceGroup), cloudServiceGroup.hasPort() ? cloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    public DockerizedService(ICloudServiceGroup cloudServiceGroup, List<ITemplate> templates) {
        this(templates, cloudServiceGroup, TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getFreeServiceId(cloudServiceGroup), cloudServiceGroup.hasPort() ? cloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), cloudServiceGroup.getMaxPlayers());
    }

    public DockerizedService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port) {
        this(templates, cloudServiceGroup, serviceId != -1 ? serviceId : TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getFreeServiceId(cloudServiceGroup), port, cloudServiceGroup.getMaxPlayers());
    }

    public DockerizedService(List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, int serviceId, int port, int maxPlayers) {
        this(cloudServiceGroup.getGroupName(), templates, cloudServiceGroup, cloudServiceGroup.getServiceType(), serviceId, port, maxPlayers, cloudServiceGroup.getMemory());
    }

    public DockerizedService(String serviceName, List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, ServiceType serviceType, int serviceId, int port, int maxPlayers, int maxMemory) {
        this(cloudServiceGroup.getGroupName(), templates, cloudServiceGroup, cloudServiceGroup.getServiceType(), serviceId, port, maxPlayers, cloudServiceGroup.getMemory(), new HashMap<>());
    }

    public DockerizedService(String serviceName, List<ITemplate> templates, ICloudServiceGroup cloudServiceGroup, ServiceType serviceType, int serviceId, int port, int maxPlayers, int maxMemory, HashMap<String, Object> propertyMap) {
        this.serviceGroup = cloudServiceGroup;
        this.serviceId = serviceId;
        this.name = serviceName;
        this.serviceType = serviceType;
        this.serviceState = ServiceState.PREPARING;
        this.templates = new LinkedList<>(templates);
        this.folder = serviceGroup.isStatic() ? new File("static//" + getServiceName()) : new File("servers//" + getServiceName());
        this.propertyMap = propertyMap;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.maxMemory = maxMemory;
        this.usedMemory = 0;
        this.onlinePlayers = 0;
        serviceGroup.getTemplates().stream().filter(template -> !this.templates.contains(template)).forEach(this.templates::add);
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().addService(this);
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().putServiceId(cloudServiceGroup, serviceId);
    }

    public boolean alive() {
        if (this.containerId != null) {
            try {
                InspectContainerResponse.ContainerState result = TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient().inspectContainerCmd(this.containerId).withSize(false).exec().getState();
                return result.getRunning() != null && result.getRunning();
            } catch (NotFoundException ignored) {}
        }
        return false;
    }

    @SneakyThrows
    public void prepare() {
        this.folder.mkdirs();
        FileUtils.copyFileToDirectory(new File(serviceGroup.getServiceType() == ServiceType.Lobby || serviceGroup.getServiceType() == ServiceType.Server ? "data//versions//spigot.yml" : "data//versions//velocity.toml"), folder);
        FileUtils.copyDirectory(new File(serviceGroup.getServiceType() == ServiceType.Lobby || serviceGroup.getServiceType() == ServiceType.Server ? "templates//Global//server" : "templates//Global//proxy"), folder);
        FileUtils.copyFileToDirectory(new File("data//versions//teriumcloud-plugin.jar"), serviceGroup.isStatic() ? new File("static//" + getServiceName() + "//plugins") : new File("servers//" + getServiceName() + "//plugins"));
        templates.forEach(template -> {
            try {
                FileUtils.copyDirectory(template.getPath().toFile(), folder);
            } catch (IOException ignored) {
            }
        });
        TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().stream().filter(module -> module.getModuleType() == ModuleType.valueOf(getServiceType().name()) || module.getModuleType() == ModuleType.ALL).forEach(module -> {
            try {
                FileUtils.copyFileToDirectory(new File("modules//" + module.getFileName()), serviceGroup.isStatic() ? new File("static//" + getServiceName() + "//plugins") : new File("servers//" + getServiceName() + "//plugins"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        AtomicBoolean hasJarFile = new AtomicBoolean(false);
        if(!ServerVersions.valueOf(serviceGroup.getVersion().toUpperCase().replace("-", "_").replace(".", "_")).equals(ServerVersions.MINESTOM)) {
            Arrays.stream(folder.listFiles()).forEach(file -> {
                if (file.getName().contains(".jar"))
                    hasJarFile.set(true);
            });

            if (!hasJarFile.get()) {
                try {
                    FileUtils.copyURLToFile(new URL(ServerVersions.getLatestVersion(ServerVersions.valueOf(serviceGroup.getVersion().toUpperCase().replace("-", "_").replace(".", "_").replace(".", "_").replace("-", "_")))), serviceGroup.isStatic() ? new File("static//" + getServiceName() + "//" + serviceGroup.getVersion() + ".jar") : new File("servers//" + getServiceName() + "//" + serviceGroup.getVersion() + ".jar"));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            Arrays.stream(folder.listFiles()).forEach(file -> {
                if (file.getName().contains("minestom.jar"))
                    hasJarFile.set(true);
            });

            if(!hasJarFile.get()) {
                Logger.log("No minestom.jar found in service-directory.", LogType.ERROR);
            }
        }

        String javaImage = TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().getJson().get(serviceGroup.getGroupName()).getAsJsonObject().get("java-image").getAsString();
        String[] javaImageSplited = javaImage.split(":");

        if(needsImagePull(javaImage)) {
            TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Starting of pulling docker image '" + javaImage + "'...", LogType.INFO);
            try {
                TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient().pullImageCmd(javaImageSplited[0])
                        .withTag(javaImageSplited[1])
                        .start().awaitCompletion();
                TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Sucessfully pulled docker image '" + javaImage + "'.", LogType.INFO);
            } catch (Exception exception) {
                TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Detected error while pulling docker image '" + javaImage + "'.", LogType.ERROR);
            }
        }
    }

    @SneakyThrows
    private void systemStart() {
        if ((serviceGroup.getServiceType() == ServiceType.Lobby || serviceGroup.getServiceType() == ServiceType.Server)) {
            if(!ServerVersions.valueOf(serviceGroup.getVersion().toUpperCase().replace("-", "_").replace(".", "_")).equals(ServerVersions.MINESTOM))
                return;

            Logger.log("Service '§b" + getServiceName() + "§f' is starting.", LogType.INFO);
            Properties properties = new Properties();
            File serverProperties = new File(this.folder, "server.properties");
            properties.setProperty("server-name", getServiceName());
            properties.setProperty("server-port", getPort() + "");
            properties.setProperty("server-ip", TeriumAPI.getTeriumAPI().getProvider().getThisNode().getAddress().getAddress().getHostAddress());
            properties.setProperty("online-mode", "false");
            properties.setProperty("max-players", maxPlayers + "");

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
            if(!ServerVersions.valueOf(serviceGroup.getVersion().toUpperCase().replace("-", "_").replace(".", "_")).equals(ServerVersions.MINESTOM))
                return;

            Logger.log("Service '§b" + getServiceName() + "§f' is starting on port " + port + ".", LogType.INFO);
            if(!serviceGroup.getVersion().contains("bungeecord")) {
                this.replaceInFile(new File(this.folder, "velocity.toml"), "%name%", getServiceName());
                this.replaceInFile(new File(this.folder, "velocity.toml"), "%port%", port + "");
                this.replaceInFile(new File(this.folder, "velocity.toml"), "%max_players%", serviceGroup.getMaxPlayers() + "");
            } else {
                this.replaceInFile(new File(this.folder, "config.yml"), "%port%", port + "");
                this.replaceInFile(new File(this.folder, "config.yml"), "%max_players%", serviceGroup.getMaxPlayers() + "");
            }
        }

        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceAdd(getServiceName(), serviceId, port, maxPlayers, getMaxMemory(),
                getServiceNode().getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), propertyMap));

        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().callEvent(new CloudServiceStartingEvent(this));

        DockerClient dockerClient = TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient();
        this.containerId = dockerClient.createContainerCmd(TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().getJson().get(serviceGroup.getGroupName()).getAsJsonObject().get("java-image").getAsString())
                .withTty(true)
                .withStdinOpen(true)
                .withStdInOnce(false)
                .withName(getServiceName() + "-" + UUID.randomUUID().toString().replace("-", ""))
                .withWorkingDir("/app")
                .withStopSignal("SIGTERM")
                .withHostConfig(HostConfig.newHostConfig()
                        .withBinds(new Bind(folder.getAbsolutePath(), new Volume("/app")))
                        .withCapDrop(EnumSet.of(
                                Capability.MKNOD,
                                Capability.FSETID,
                                Capability.FOWNER,
                                Capability.SETPCAP,
                                Capability.SETFCAP,
                                Capability.NET_RAW,
                                Capability.SYS_CHROOT,
                                Capability.AUDIT_WRITE,
                                Capability.DAC_OVERRIDE,
                                Capability.NET_BIND_SERVICE
                        ).toArray(Capability[]::new))
                        .withRestartPolicy(RestartPolicy.noRestart())
                        .withNetworkMode("host")
                        .withPortBindings(PortBinding.parse(TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().getPort() + ":" + TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().getPort()),
                                PortBinding.parse(port + ":" + port)))
                .withExposedPorts(ExposedPort.tcp(port), ExposedPort.udp(port), ExposedPort.tcp(TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().getPort()), ExposedPort.udp(TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().getPort()))
                .withEntrypoint("java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=50", "-XX:-UseAdaptiveSizePolicy", "-XX:CompileThreshold=100", "-Dio.netty.recycler.maxCapacity=0", "-Dio.netty.recycler.maxCapacity.default=0", "-Djline.terminal=jline.UnsupportedTerminal", "-Xmx" + serviceGroup.getMemory() + "m", "-jar", "-Dservicename=" + getServiceName(), "-Dservicenode=" + getServiceNode().getName(), "-Dnetty-address=" + TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().getHostAddress(), "-Dnetty-port=" + TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().getPort(), serviceGroup.getVersion() + ".jar")
                .exec()
                .getId();
        dockerClient.startContainerCmd(this.containerId).exec();
    }

    @SneakyThrows
    public void start() {
        Executors.newCachedThreadPool().execute(() -> {
            prepare();
            systemStart();
        });

        TeriumAPI.getTeriumAPI().getProvider().getThisNode().setUsedMemory(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().gloablUsedMemory());
        TeriumAPI.getTeriumAPI().getProvider().getThisNode().update();
    }

    @Override
    public void forceShutdown() {
        if (this.containerId != null) {
            try {
                TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient().removeContainerCmd(this.containerId).withRemoveVolumes(true).withForce(true).exec();
                TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Successfully stopped docker-container of service '§b" + getServiceName() + "§f'.", LogType.INFO);
                TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));
                delete();
            } catch (NotFoundException ignored) {}
        }
    }

    public void shutdown() {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceExecuteCommand(getServiceName(), "stop"));
        if (serviceState == ServiceState.PREPARING) {
            if (this.containerId != null) {
                try {
                    TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient().removeContainerCmd(this.containerId).withRemoveVolumes(true).withForce(true).exec();
                    TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Successfully stopped docker-container of service '§b" + getServiceName() + "§f'.", LogType.INFO);
                    TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));
                    delete();
                } catch (NotFoundException ignored) {}
            }
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (containerId != null) {
                        try {
                            TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient().removeContainerCmd(containerId).withRemoveVolumes(true).withForce(true).exec();
                            TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Successfully stopped docker-container of service '§b" + getServiceName() + "§f'.", LogType.INFO);
                            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));
                            delete();
                        } catch (NotFoundException ignored) {}
                    }
                }
            }, 4000);
        }
    }

    @SneakyThrows
    public void delete() {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().removeServiceId(serviceGroup, serviceId);
        if (!serviceGroup.isStatic())
            FileUtils.deleteDirectory(folder);
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().removeService(this);
    }

    protected boolean needsImagePull(String imageName) {
        try {
            TeriumDockerizedServices.getInstance().getDockerizedConfig().getDockerClient().inspectImageCmd(imageName).exec();
            return false;
        } catch (NotFoundException exception) {
            return true;
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
        return getServiceId() > 9 ? name + "-" + getServiceId() : name + "-" + "0" + getServiceId();
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
    public int getMaxMemory() {
        return maxMemory;
    }

    @Override
    public void update() {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutUpdateService(getServiceName(), getOnlinePlayers(), getUsedMemory(), getServiceState(), isLocked(), getPropertyMap()));
    }

    @Override
    public ICloudServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    @Override
    public INode getServiceNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getThisNode();
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