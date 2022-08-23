package cloud.terium.cloudsystem.manager;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.setup.SetupState;
import cloud.terium.cloudsystem.utils.setup.SetupStorage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Scanner;

public class SetupManager {

    private final SetupStorage setupStorage;

    public SetupManager() {
        this.setupStorage = new SetupStorage();
        Logger.log("Welcome to \u001B[0mTerium\u001B[36mCloud\u001B[0m! Please agree the minecraft eula to continue. (type: yes if you agree)", LogType.SETUP);
        readConsole();
    }

    /*
     * TODO: Make that this is not looking like CloudNet-v3
     */

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
                    case EULA -> {
                        if (input.equalsIgnoreCase("yes")) {
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.WEB_PORT);
                            setupStorage.setEula(true);
                            Logger.log("You agreed the minecraft eula! Please type now the port for your webserver. (default: 5124) (important for multi-root)", LogType.SETUP);
                        } else {
                            Logger.log("You need to agree the minecraft eula to use Terium!", LogType.SETUP);
                            try {
                                FileUtils.forceDelete(new File("config.json"));
                                Thread.sleep(2000);
                            } catch (Exception ignored) {
                            }
                            System.exit(0);
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
                            Terium.getTerium().getCloudUtils().setSetupState(SetupState.DONE);
                            setupStorage.setSpigotVersion(input);

                            Logger.log("You successfully set the service-server version to '" + input + "'.", LogType.SETUP);
                            Logger.log("Please wait a small while. Terium is starting soon...", LogType.SETUP);
                        }
                    }
                }
            }
        }).start();
    }
}
