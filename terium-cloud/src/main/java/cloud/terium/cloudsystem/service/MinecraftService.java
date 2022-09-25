package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packets.PacketPlayOutServiceAdd;
import cloud.terium.networking.packets.PacketPlayOutServiceRemove;
import cloud.terium.teriumapi.service.CloudServiceState;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class MinecraftService implements ICloudService {

    private final ICloudServiceGroup serviceGroup;
    private CloudServiceState serviceState;
    private final File folder;
    private final int port;
    private final int serviceId;
    private long usedMemory;
    private int onlinePlayers;
    private Process process;
    private final File template;
    private Thread thread;

    /*
     TODO: Looking and debug why the template folder is copied in proxy servers.
     */
    public MinecraftService(ICloudServiceGroup serviceGroup) {
        this(serviceGroup, Terium.getTerium().getServiceManager().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    public MinecraftService(ICloudServiceGroup serviceGroup, int serviceId) {
        this(serviceGroup, serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000));
    }

    public MinecraftService(ICloudServiceGroup defaultServiceGroup, int serviceId, int port) {
        this.serviceGroup = defaultServiceGroup;
        this.serviceId = serviceId;
        this.serviceState = CloudServiceState.PREPARING;
        this.template = new File("templates//" + serviceGroup.getServiceGroupName() + "//");
        this.folder = new File("servers//" + getServiceName());
        this.port = port;
        this.usedMemory = 0;
        this.onlinePlayers = 0;
    }

    @SneakyThrows
    public void start() {
        this.folder.mkdirs();
        FileUtils.copyFileToDirectory(new File("data//versions//" + (serviceGroup.getServiceType() == CloudServiceType.Lobby || serviceGroup.getServiceType() == CloudServiceType.Server ? "server.jar" : "velocity.jar")), folder);
        FileUtils.copyDirectory(new File(serviceGroup.getServiceType() == CloudServiceType.Lobby || serviceGroup.getServiceType() == CloudServiceType.Server ? "templates//Global//Server" : "templates//Global//Proxy"), folder);
        FileUtils.copyFileToDirectory(new File("data//versions//teriumbridge.jar"), new File("servers//" + getServiceName() + "//plugins//"));
        FileUtils.copyDirectory(template, folder);
        Terium.getTerium().getServiceManager().addService(this);
        Terium.getTerium().getModuleManager().getAllModules().forEach(module -> {
            try {
                switch (module.getModuleType()) {
                    case Server -> {
                        if(getServiceType() == CloudServiceType.Server || getServiceType() == CloudServiceType.Lobby) FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                    }
                    case Proxy -> {
                        if(getServiceType() == CloudServiceType.Proxy) FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                    }
                    case Lobby -> {
                        if(getServiceType() == CloudServiceType.Lobby) FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
                    }
                    case ALL -> FileUtils.copyFileToDirectory(module.getFile(), new File("servers//" + getServiceName() + "//plugins//"));
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
        // Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceForceShutdown(getServiceName()));
        MinecraftService minecraftService = this;
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
                Terium.getTerium().getServiceManager().removeService(minecraftService);
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
        return getServiceId() > 9 ? getServiceGroup().getServiceGroupName() + "-" + getServiceId() : getServiceGroup().getServiceGroupName() + "-0" + getServiceId();
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
        return ICloudService.super.getMaxPlayers();
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
    public int getMaxMemory() {
        return ICloudService.super.getMaxMemory();
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
    public CloudServiceType getServiceType() {
        return serviceGroup.getServiceType();
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