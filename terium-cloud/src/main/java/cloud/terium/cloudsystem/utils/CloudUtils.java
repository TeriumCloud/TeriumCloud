package cloud.terium.cloudsystem.utils;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.setup.SetupState;
import cloud.terium.cloudsystem.utils.setup.SetupStorage;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CloudUtils {

    private final String startMessage;
    private final List<ICloudPlayer> playerList;
    private boolean running;
    private boolean setup;
    private SetupState setupState;
    private boolean isInScreen;

    public CloudUtils() {
        this.startMessage =
                """
                        \u001B[0m_______ _______  ______ _____ _     _ _______ \u001B[36m_______         _____  _     _ ______\s
                        \u001B[0m   |    |______ |_____/   |   |     | |  |  | \u001B[36m|       |      |     | |     | |     \\
                        \u001B[0m   |    |______ |    \\_ __|__ |_____| |  |  | \u001B[36m|_____  |_____ |_____| |_____| |_____/
                                                                                                    \s
                        \u001B[0m> Terium by Jannik Hegemann aka. ByRaudy\s
                        \u001B[0m> Discord: terium.cloud/discord | Twitter: \u001B[36m@teriumcloud \u001B[0m
                        """;
        this.running = true;
        this.setup = false;
        this.isInScreen = false;
        this.playerList = new ArrayList<>();
    }

    public void startSetup() {
        this.setup = true;
        this.setupState = SetupState.STARTING;
    }

    @SneakyThrows
    public void shutdownCloud() {
        Logger.log("Trying to stop Terium...", LogType.INFO);

        running = false;
        Terium.getTerium().getServiceManager().getMinecraftServices().forEach(ICloudService::shutdown);
        Logger.log("Successfully stopped all services.", LogType.INFO);
        Thread.sleep(1000);
        Terium.getTerium().getDefaultTeriumNetworking().getServer().getChannel().close().sync();
        Logger.log("Successfully stopped terium-server.", LogType.INFO);
        Terium.getTerium().getConfigManager().resetPort();
        Logger.log("Successfully reset terium-port.", LogType.INFO);

        FileUtils.deleteDirectory(new File("servers//"));
        Logger.log("Successfully deleted server folder.", LogType.INFO);
        FileUtils.deleteDirectory(new File("data//cache//"));
        Logger.log("Successfully deleted data/cache folder.", LogType.INFO);
        Thread.sleep(300);
        Logger.log("Successfully stopped Terium. Goodbye!", LogType.INFO);
        Thread.sleep(1000);
        System.exit(0);
    }

    @SneakyThrows
    public void checkLicense() {
        Logger.licenseLog("Trying to check license...", LogType.INFO);
        if (getHttpResponse("licence?key=" + Terium.getTerium().getConfigManager().getString("license")) != null) {
            Logger.licenseLog("License is accessible.", LogType.INFO);
            System.out.println(" ");
        } else {
            Logger.licenseLog("Your license isn't accessible.", LogType.ERROR);
            Logger.licenseLog("Please typ a accessible license. Or are you one of the illegal owners of the cloud? ;)", LogType.ERROR);
            Thread.sleep(5000);
            System.exit(0);
        }
    }

    @SneakyThrows
    public String getHttpResponse(String parameta) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://45.142.114.48:8080/" + parameta).openConnection();
        httpURLConnection.setRequestMethod("GET");

        try {
            httpURLConnection.connect();
        } catch (ConnectException exception) {
            Logger.licenseLog("The cloudrestapi is not available.", LogType.ERROR);
            Logger.licenseLog("Please contact Jannik#9708 on discord.", LogType.ERROR);
            Thread.sleep(5000);
            shutdownCloud();
            return null;
        }

        String line = "";
        StringBuilder response = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
        } catch (FileNotFoundException exception) {
            return null;
        }

        if (response.toString().equals("This license didn't exist.")) return null;
        return response.toString();
    }

    public long getUsedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public long getmaxMemory() {
        return (Runtime.getRuntime().maxMemory()) / (1024 * 1024);
    }
}