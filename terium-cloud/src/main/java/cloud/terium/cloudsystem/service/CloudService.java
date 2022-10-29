package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packets.PacketPlayOutServiceAdd;
import cloud.terium.networking.packets.PacketPlayOutServiceRemove;
import cloud.terium.networking.packets.PacketPlayOutServiceUnlock;
import cloud.terium.networking.packets.PacketPlayOutUpdateService;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.CloudServiceState;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class CloudService implements ICloudService {

    private final ICloudServiceGroup serviceGroup;
    private CloudServiceState serviceState;
    private CloudServiceType serviceType;
    private final String name;
    private final File folder;
    private final int port;
    private final int serviceId;
    private final int maxPlayers;
    private final int maxMemory;
    private long usedMemory;
    private int onlinePlayers;
    private boolean locked;
    private Process process;
    private final ITemplate template;
    private Thread outputThread;
    private Thread thread;

    public CloudService(ICloudServiceGroup iCloudServiceGroup) {
        this(iCloudServiceGroup.getTemplate(), iCloudServiceGroup, Terium.getTerium().getServiceManager().getFreeServiceId(iCloudServiceGroup), iCloudServiceGroup.getPort());
    }

    public CloudService(ITemplate template, ICloudServiceGroup iCloudServiceGroup, int serviceId, int port) {
        this(template, iCloudServiceGroup, serviceId, port, iCloudServiceGroup.getMaximumPlayers());
    }

    public CloudService(ITemplate template, ICloudServiceGroup iCloudServiceGroup, int serviceId, int port, int maxPlayers) {
        this(iCloudServiceGroup.getServiceGroupName(), template, iCloudServiceGroup, iCloudServiceGroup.getServiceType(), serviceId, port, maxPlayers, iCloudServiceGroup.getMemory());
    }

    public CloudService(String serviceName, ITemplate template, ICloudServiceGroup iCloudServiceGroup, CloudServiceType serviceType, int serviceId, int port, int maxPlayers, int maxMemory) {
        this.serviceGroup = iCloudServiceGroup;
        this.serviceId = serviceId;
        this.name = serviceName;
        this.serviceType = serviceType;
        this.serviceState = CloudServiceState.PREPARING;
        this.template = template;
        this.folder = new File("servers//" + getServiceName());
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.maxMemory = maxMemory;
        this.usedMemory = 0;
        this.onlinePlayers = 0;
        Terium.getTerium().getScreenManager().addCloudService(this);
        Logger.log("Successfully created service " + getServiceName() + ".", LogType.INFO);
    }

    @SneakyThrows
    public void start() {
        this.folder.mkdirs();
        FileUtils.copyFileToDirectory(new File("data//versions//" + (serviceGroup.getServiceType() == CloudServiceType.Lobby || serviceGroup.getServiceType() == CloudServiceType.Server ? "server.jar" : "velocity.jar")), folder);
        FileUtils.copyDirectory(new File(serviceGroup.getServiceType() == CloudServiceType.Lobby || serviceGroup.getServiceType() == CloudServiceType.Server ? "templates//Global//server" : "templates//Global//proxy"), folder);
        FileUtils.copyFileToDirectory(new File("data//versions//teriumbridge.jar"), new File("servers//" + getServiceName() + "//plugins//"));
        FileUtils.copyDirectory(template.getPath().toFile(), folder);
        Terium.getTerium().getServiceManager().addService(this);
        Terium.getTerium().getModuleManager().getAllModules().forEach(module -> {
            try {
                switch (module.getModuleType()) {
                    case Server -> {
                        if (getServiceType() == CloudServiceType.Server || getServiceType() == CloudServiceType.Lobby)
                            FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                    }
                    case Proxy -> {
                        if (getServiceType() == CloudServiceType.Proxy)
                            FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                    }
                    case Lobby -> {
                        if (getServiceType() == CloudServiceType.Lobby)
                            FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                    }
                    case ALL ->
                            FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        if (serviceGroup.getServiceType() == CloudServiceType.Lobby || serviceGroup.getServiceType() == CloudServiceType.Server) {
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
                properties.store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), "Auto eula agreement by Terium.");
            }
        } else {
            Logger.log("The service '" + getServiceName() + "' is starting on port " + port + ".", LogType.INFO);
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%name%", getServiceName());
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%port%", port + "");
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%max_players%", serviceGroup.getMaximumPlayers() + "");
        }

        if (!serviceGroup.getServiceType().equals(CloudServiceType.Proxy))
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceAdd(getServiceName(), getServiceGroup().getServiceGroupName(), getServiceId(), getPort()));

        this.thread = new Thread(() -> {
            String[] command = new String[]{"java", "-jar", "-Xmx" + serviceGroup.getMemory() + "m", serviceGroup.getServiceType() == CloudServiceType.Lobby || serviceGroup.getServiceType() == CloudServiceType.Server ? "server.jar" : "velocity.jar", "nogui"};
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

            Terium.getTerium().getServiceManager().removeService(this);
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));
            try {
                FileUtils.deleteDirectory(this.folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
        });
        this.thread.start();
    }

    public void shutdown() {
        CloudService cloudService = this;
        if (Terium.getTerium().getCloudUtils().isInScreen() && Terium.getTerium().getScreenManager().getCurrentScreen().equals(this))
            toggleScreen();
        Logger.log("Trying to stop service '" + getServiceName() + "'... [MinecraftService#forceShutdown]", LogType.INFO);
        if (!serviceGroup.getServiceType().equals(CloudServiceType.Proxy))
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));

        thread.stop();
        process.destroyForcibly();
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                FileUtils.deleteDirectory(folder);
                Terium.getTerium().getServiceManager().removeService(cloudService);
                Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
            }
        }, 5000);
    }

    /*@SneakyThrows
    @Override
    public void forceShutdown() {
        MinecraftService minecraftService = this;
        Logger.log("Trying to stop service '" + getServiceName() + "'. [MinecraftService#forceShutdown]", LogType.INFO);
        if (!serviceGroup.getServiceType().equals(CloudServiceType.Proxy))
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(getServiceName()));

        thread.stop();
        process.destroyForcibly();
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                FileUtils.deleteDirectory(folder);
                Terium.getTerium().getServiceManager().removeService(minecraftService);
                Logger.log("Successfully stopped service '" + getServiceName() + "'.", LogType.INFO);
            }
        }, 5000);
    }*/

    public void toggleScreen() {
        if (!Terium.getTerium().getCloudUtils().isInScreen()) {
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
                    Terium.getTerium().getScreenManager().addLogToScreen(this, "[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + LogType.SCREEN.getPrefix() + line);
                }
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Logger.log("You're now inside of " + getServiceName() + ".", LogType.INFO);
            Terium.getTerium().getCloudUtils().setInScreen(true);
            Terium.getTerium().getScreenManager().setCurrentScreen(this);
            if (Terium.getTerium().getScreenManager().getLogsFromService(this) != null)
                Terium.getTerium().getScreenManager().getLogsFromService(this).forEach(log -> Logger.log(log, LogType.SCREEN));
            outputThread.start();
        } else {
            outputThread.stop();
            Terium.getTerium().getScreenManager().setCurrentScreen(null);
            Terium.getTerium().getCloudUtils().setInScreen(false);
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
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    @Override
    public long getUsedMemory() {
        return usedMemory;
    }

    @Override
    public void update() {

    }

    @Override
    public int getMaxMemory() {
        return maxMemory;
    }

    @Override
    public ICloudServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    @Override
    public CloudServiceState getServiceState() {
        return serviceState;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public CloudServiceType getServiceType() {
        return serviceGroup.getServiceType();
    }

    @Override
    public void setLocked(boolean b) {
        this.locked = b;
    }

    @Override
    public void setOnlinePlayers(int i) {
        this.onlinePlayers = i;
    }

    @Override
    public void setUsedMemory(long i) {
        this.usedMemory = i;
    }

    @Override
    public void setServiceState(CloudServiceState cloudServiceState) {
        this.serviceState = cloudServiceState;
    }
}