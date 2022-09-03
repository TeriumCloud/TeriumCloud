package cloud.terium.cloudsystem.manager;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.setup.SetupState;
import cloud.terium.cloudsystem.utils.setup.SetupStorage;
import cloud.terium.cloudsystem.utils.setup.SetupType;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Scanner;

public class SetupManager {

    private final SetupStorage setupStorage;

    public SetupManager() {
        this.setupStorage = new SetupStorage();
        Logger.log("Welcome to \u001B[0mTerium\u001B[36mCloud\u001B[0m! Please choose one of two setup types. (automatic or manual)", LogType.SETUP);
        readConsole();
    }

    private void readConsole() {
        final Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (scanner.hasNextLine()) {
                final String input = scanner.nextLine();

                if (input.equalsIgnoreCase("stop")) {
                    Logger.log("Trying to stop the cloud...", LogType.SETUP);
                    try {
                        FileUtils.forceDelete(new File("config.json"));
                        Thread.sleep(1000);
                    } catch (Exception ignored) {
                    }
                    System.exit(0);
                }

                switch (Terium.getTerium().getCloudUtils().getSetupState()) {
                    case STARTING -> {
                        switch (input) {
                            case "automatic" -> {
                                setupStorage.setSetupType(SetupType.AUTOMATIC);

                                Logger.log("The terium-cloud is setting up automatic. Please wait a moment...", LogType.SETUP);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException exception) {
                                    exception.printStackTrace();
                                }
                                Logger.log("Terium is trying to set the web server port.", LogType.SETUP);
                                Terium.getTerium().getConfigManager().getJson().addProperty("web_port", 5124);
                                Terium.getTerium().getConfigManager().save();
                                Logger.log("Successfully set the web server port(5124).", LogType.SETUP);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException exception) {
                                    exception.printStackTrace();
                                }
                                Logger.log("Terium is trying to create a lobby and a porxy group...", LogType.SETUP);
                                new DefaultProxyGroup("Proxy", "PROXY", "Node-01", "velocity", true, 25565, 10, 128, 1, 1);
                                Logger.log("Successfully created Proxy(proxy group | Node-01, 10 players, 128 memory, velocity).", LogType.SETUP);
                                new DefaultLobbyGroup("Lobby", "LOBBY", "Node-01", "paperspigot-1.19.2", true, 20, 512, 1, 1);
                                Logger.log("Successfully created Lobby(lobby group | Node-01, 20 players, 512 memory, paperspigot-1.19.2).", LogType.SETUP);

                                Terium.getTerium().getCloudUtils().setSetupState(SetupState.DONE);
                                Logger.log("Please wait a small while. Terium is starting soon...", LogType.SETUP);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException exception) {
                                    exception.printStackTrace();
                                }
                                new Terium();
                            }
                            case "manual" -> {
                                setupStorage.setSetupType(SetupType.MANUAL);

                                Logger.log("The terium-cloud is setting up manual. Please wait a moment...", LogType.SETUP);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException exception) {
                                    exception.printStackTrace();
                                }
                                Logger.log("Terium is trying to set the web server port.", LogType.SETUP);
                                Terium.getTerium().getConfigManager().getJson().addProperty("web_port", 5124);
                                Terium.getTerium().getConfigManager().save();
                                Logger.log("Successfully set the web server port(5124).", LogType.SETUP);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException exception) {
                                    exception.printStackTrace();
                                }
                                Logger.log("Please type a version for your proxy server.", LogType.SETUP);
                                Logger.log("Available versions: bungeecord, waterfall or velocity", LogType.SETUP);
                                Terium.getTerium().getCloudUtils().setSetupState(SetupState.PROXY_VERSION);
                            }
                        }
                    }
                    case PROXY_VERSION -> {
                        if ("bungeecord".equals(input) || "waterfall".equals(input) || "velocity".equals(input)) {
                            setupStorage.setProxyVersion(input);
                            Logger.log("You choosed '" + input + "' as proxy version.", LogType.SETUP);
                            Logger.log("Terium is trying to create a proxy group with " + input + ". Please wait a moment...", LogType.SETUP);
                            new DefaultProxyGroup("Proxy", "PROXY", "Node-01", input, true, 25565, 100, 128, 1, 1);
                            Logger.log("Terium successfully created a proxy group with " + input + ".", LogType.SETUP);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException exception) {
                                exception.printStackTrace();
                            }
                            Logger.log("Now please type a version for your server groups. (lobby & servers)", LogType.SETUP);
                            Logger.log("Available versions: spigot-1.16.5, spigot-1.17.1, spigot-1.18.2, spigot-1.19.2, paperspigot-1.16.5, paperspigot-1.17.1, paperspigot-1.18.2, paperspigot-1.19.2", LogType.SETUP);
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.SPIGOT_VERSION);
                        }
                    }
                    case SPIGOT_VERSION -> {
                        if ("spigot-1.16.5".equals(input) || "spigot-1.17.1".equals(input) || "spigot-1.18.2".equals(input) ||
                                "spigot-1.19.2".equals(input) || "paperspigot-1.16.5".equals(input) || "paperspigot-1.17.1".equals(input) || "paperspigot-1.18.2".equals(input) || "paperspigot-1.19.2".equals(input)) {
                            setupStorage.setSpigotVersion(input);
                            Logger.log("You choosed '" + input + "' as server version.", LogType.SETUP);
                            Logger.log("Terium is trying to create a lobby group with " + input + ". Please wait a moment...", LogType.SETUP);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException exception) {
                                exception.printStackTrace();
                            }
                            new DefaultLobbyGroup("Lobby", "MAIN-LOBBY", "Node-01", input, true, 20, 512, 1, 1);
                            Logger.log("Terium successfully created a lobby group with " + input + ".", LogType.SETUP);
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.DONE);
                            Logger.log("Please wait a small while. Terium is starting soon...", LogType.INFO);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException exception) {
                                exception.printStackTrace();
                            }
                            new Terium();
                        }
                    }
                }
            }
        }).start();
    }
}
