package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.networking.json.DefaultJsonService;
import cloud.terium.networking.packets.PacketPlayOutServiceAdd;
import cloud.terium.networking.packets.PacketPlayOutServiceForceShutdown;
import cloud.terium.networking.packets.PacketPlayOutServiceRemove;
import cloud.terium.cloudsystem.service.group.DefaultServiceGroup;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class MinecraftService implements IService {

    private final DefaultServiceGroup defaultServiceGroup;
    private final File template;
    private final File folder;
    private final int port;
    private final int serviceId;
    private boolean online;
    private int usedMemory;
    private int onlinePlayers;
    private Process process;
    private Thread thread;

    public MinecraftService(DefaultServiceGroup defaultServiceGroup) {
        this(defaultServiceGroup, Terium.getTerium().getServiceManager().getFreeServiceId(defaultServiceGroup), defaultServiceGroup.hasPort() ? defaultServiceGroup.port() : new Random().nextInt(20000, 50000));
    }

    public MinecraftService(DefaultServiceGroup defaultServiceGroup, int serviceId) {
        this(defaultServiceGroup, serviceId, defaultServiceGroup.hasPort() ? defaultServiceGroup.port() : new Random().nextInt(20000, 50000));
    }

    public MinecraftService(DefaultServiceGroup defaultServiceGroup, int serviceId, int port) {
        this.defaultServiceGroup = defaultServiceGroup;
        this.serviceId = serviceId;
        this.template = new File("templates//" + defaultServiceGroup.name() + "//");
        this.folder = new File("servers//" + serviceName());
        this.port = port;
        this.usedMemory = 0;
        this.onlinePlayers = 0;
        this.online = false;
    }

    @SneakyThrows
    public void start() {
        this.folder.mkdirs();
        FileUtils.copyFileToDirectory(new File("data//versions//" + (defaultServiceGroup.serviceType() == ServiceType.Lobby || defaultServiceGroup.serviceType() == ServiceType.Server ? "server.jar" : "velocity.jar")), folder);
        FileUtils.copyDirectory(new File(defaultServiceGroup.serviceType() == ServiceType.Lobby || defaultServiceGroup.serviceType() == ServiceType.Server ? "templates//Global//Server" : "templates//Global//Proxy"), folder);
        FileUtils.copyDirectory(template, folder);
        FileUtils.copyFileToDirectory(new File("data//versions//teriumbridge.jar"), new File("servers//" + serviceName() + "//plugins//"));
        Terium.getTerium().getServiceManager().addService(this);

        if (defaultServiceGroup.serviceType() == ServiceType.Lobby || defaultServiceGroup.serviceType() == ServiceType.Server) {
            Logger.log("The service '" + serviceName() + "' is starting on port " + port + ".", LogType.INFO);

            Properties properties = new Properties();
            File serverProperties = new File(this.folder, "server.properties");
            properties.setProperty("server-name", serviceName());
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
            Logger.log("The service '" + serviceName() + "' is starting on port " + port + ".", LogType.INFO);
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%name%", serviceName());
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%port%", port + "");
            this.replaceInFile(new File(this.folder, "velocity.toml"), "%max_players%", defaultServiceGroup.maximumPlayers() + "");
        }

        if (!defaultServiceGroup.serviceType().equals(ServiceType.Proxy))
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceAdd(serviceName(), defaultServiceGroup().name(), serviceId(), port()));

        this.thread = new Thread(() -> {
            String[] command = new String[]{"java", "-jar", "-Xmx" + defaultServiceGroup.memory() + "m", defaultServiceGroup.serviceType() == ServiceType.Lobby || defaultServiceGroup.serviceType() == ServiceType.Server ? "server.jar" : "velocity.jar", "nogui"};
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
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(serviceName()));
            try {
                FileUtils.deleteDirectory(this.folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.log("Successfully stopped service '" + serviceName() + "'.", LogType.INFO);
        });
        this.thread.start();
    }

    public void shutdown() {
        if(!this.online) {
            forceShutdown();
            return;
        }

        Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceForceShutdown(serviceName()));
        Logger.log("Trying to stop service '" + serviceName() + "'.", LogType.INFO);
    }

    @SneakyThrows
    public void forceShutdown() {
        MinecraftService minecraftService = this;
        Logger.log("Trying to stop service '" + serviceName() + "'.", LogType.INFO);
        if (!defaultServiceGroup.serviceType().equals(ServiceType.Proxy))
            Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceRemove(serviceName()));

        thread.stop();
        process.destroyForcibly();
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                FileUtils.deleteDirectory(folder);
                Terium.getTerium().getServiceManager().removeService(minecraftService);
                Logger.log("Successfully stopped service '" + serviceName() + "'.", LogType.INFO);
            }
        }, 5000);
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

    public void updateUsedMemory(int memory) {
        this.usedMemory = memory;
        new DefaultJsonService(this, true).updateUsedMemory(memory);
    }

    public void updateOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        new DefaultJsonService(this, true).updateOnlinePlayers(onlinePlayers);
    }

    public void online(boolean online) {
        this.online = online;
    }

    @Override
    public String serviceName() {
        return getServiceId() > 9 ? defaultServiceGroup.name() + "-" + getServiceId() : defaultServiceGroup.name() + "-0" + getServiceId();
    }

    @Override
    public boolean online() {
        return online;
    }

    @Override
    public int serviceId() {
        return serviceId;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public int maxPlayers() {
        return defaultServiceGroup.maximalServices();
    }

    @Override
    public int onlinePlayers() {
        return onlinePlayers;
    }

    @Override
    public int usedMemory() {
        return usedMemory;
    }

    @Override
    public int maxMemory() {
        return defaultServiceGroup.memory();
    }

    @Override
    public DefaultServiceGroup defaultServiceGroup() {
        return defaultServiceGroup;
    }

    @Override
    public ServiceType serviceType() {
        return defaultServiceGroup.serviceType();
    }
}