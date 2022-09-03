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
        Logger.log("Welcome to \u001B[0mTerium\u001B[36mCloud\u001B[0m! Please choose one of three setup types. (automatic, semi-automatic or manual)", LogType.SETUP);
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
                                new DefaultProxyGroup("Proxy", "PROXY", "Node-01", true, 25565, 10, 128, 1, 1);
                                Logger.log("Successfully created Proxy(proxy group | Node-01, 10 players, 128 memory, velocity-latest).", LogType.SETUP);
                                new DefaultLobbyGroup("Lobby", "LOBBY", "Node-01", true, 20, 512, 1, 1);
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
                            case "semi-automatic" -> {
                                setupStorage.setSetupType(SetupType.SEMI_AUTOMATIC);

                                Logger.log("The terium-cloud is setting up semi-automatic. Please wait a moment...", LogType.SETUP);
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
                                Logger.log("Available versions: bungeecord, waterfall or velocity-latest", LogType.SETUP);
                            }
                            case "manual" -> {

                            }
                        }

                        switch (setupStorage.getSetupType()) {
                            case SEMI_AUTOMATIC -> {
                                switch (input) {
                                    case "bungeecord" -> {
                                        Logger.log("You choosed 'bungeecord' as proxy version.", LogType.SETUP);
                                        Logger.log("Terium is trying to download 'bungeecord.jar'. Please wait a moment...", LogType.DOWNLOAD);
                                    }
                                }
                            }
                        }
                    }
                    case EULA -> {
                        if (input.equalsIgnoreCase("yes")) {
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.WEB_PORT);
                            setupStorage.setEula(true);
                            Logger.log("You agreed the minecraft eula! Please type now the port for your webserver. (default: 5124) (important for multi-root)", LogType.SETUP);
                        } else {
                            Logger.log("You need to agree the minecraft eula to use Terium!", LogType.SETUP);
                        }
                    }
                    case WEB_PORT -> {
                        if (input.matches("[0-9]+") && input.length() >= 4) {
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.PROXY_PORT);
                            setupStorage.setWebPort(Integer.parseInt(input));
                            Logger.log("You successfully set the port for the webserver. Now please type the port your proxy should start on. (default: 25565)", LogType.SETUP);
                        }
                    }
                    case PROXY_PORT -> {
                        if (input.matches("[0-9]+") && input.length() >= 4) {
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.PROXY_VERSION);
                            setupStorage.setProxyPort(Integer.parseInt(input));
                            Logger.log("You successfully set the port for the proxy-server. Now please type your proxy version. You can choose: VELOCITY, BUNGEECORD and WATERFALL (we recommend 'velocity')", LogType.SETUP);
                        }
                    }
                    case PROXY_VERSION -> {
                        if (input.equalsIgnoreCase("BUNGEECORD") || input.equalsIgnoreCase("WATERFALL") || input.equalsIgnoreCase("VELOCITY")) {
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.SPIGOT_VERSION);
                            setupStorage.setProxyVersion(input);
                            Logger.log("You successfully set the proxy-server version to '" + input + "'. Now please type your server version. Please choose one of the versions on the bottom. (we recommend 'paperspigot-1.19.2')", LogType.SETUP);
                            Logger.log("spigot-1.12.2", LogType.SETUP);
                            Logger.log("spigot-1.14.4", LogType.SETUP);
                            Logger.log("spigot-1.15.2", LogType.SETUP);
                            Logger.log("spigot-1.16.5", LogType.SETUP);
                            Logger.log("spigot-1.17.1", LogType.SETUP);
                            Logger.log("spigot-1.18.2", LogType.SETUP);
                            Logger.log("spigot-1.19.2", LogType.SETUP);
                            Logger.log("paperspigot-1.12.2", LogType.SETUP);
                            Logger.log("paperspigot-1.14.4", LogType.SETUP);
                            Logger.log("paperspigot-1.15.2", LogType.SETUP);
                            Logger.log("paperspigot-1.16.5", LogType.SETUP);
                            Logger.log("paperspigot-1.17.1", LogType.SETUP);
                            Logger.log("paperspigot-1.18.2", LogType.SETUP);
                            Logger.log("paperspigot-1.19.2", LogType.SETUP);
                        }
                    }
                    case SPIGOT_VERSION -> {
                        if (input.equalsIgnoreCase("spigot-1.12.2") || input.equalsIgnoreCase("spigot-1.14.4")
                                || input.equalsIgnoreCase("spigot-1.15.2") || input.equalsIgnoreCase("spigot-1.16.5")
                                || input.equalsIgnoreCase("spigot-1.17.1") || input.equalsIgnoreCase("spigot-1.18.2")
                                || input.equalsIgnoreCase("spigot-1.19.2") || input.equalsIgnoreCase("paperspigot-1.12.2")
                                || input.equalsIgnoreCase("paperspigot-1.14.2") || input.equalsIgnoreCase("paperspigot-1.15.2")
                                || input.equalsIgnoreCase("paperspigot-1.16.5") || input.equalsIgnoreCase("paperspigot-1.17.1")
                                || input.equalsIgnoreCase("paperspigot-1.18.2") || input.equalsIgnoreCase("paperspigot-1.19.2")) {
                            setupStorage.setSpigotVersion(input);

                            Logger.log("You successfully set the service-server version to '" + input + "'.", LogType.SETUP);
                            Terium.getTerium().getConfigManager().getJson().addProperty("web_port", setupStorage.getWebPort());
                            Terium.getTerium().getConfigManager().save();
                            new DefaultProxyGroup("Proxy", "PROXY", "Node-01", true, setupStorage.getProxyPort(), 10, 128, 1, 1);
                            new DefaultLobbyGroup("Lobby", "LOBBY", "Node-01", true, 20, 512, 1, 1);

                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.DONE);
                            Logger.log("Please wait a small while. Terium is starting soon...", LogType.SETUP);
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
