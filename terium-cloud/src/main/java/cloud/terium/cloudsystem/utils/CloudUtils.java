package cloud.terium.cloudsystem.utils;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.cloudsystem.utils.setup.SetupState;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.player.ICloudPlayer;
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

    private final List<ICloudPlayer> playerList;
    private final String version;
    private boolean running;
    private boolean setup;
    private SetupState setupState;
    private boolean isInScreen;

    public CloudUtils() {
        this.running = true;
        this.setup = false;
        this.isInScreen = false;
        this.version = "1.0.0-DEVELOPMENT";
        this.playerList = new ArrayList<>();
    }

    public void startSetup() {
        this.setup = true;
        this.setupState = SetupState.STARTING;
    }

    @SneakyThrows
    public void checkLicense() {
        Logger.licenseLog("Trying to check license...", LogType.INFO);
        /*if (getHttpResponse("licence?key=" + TeriumCloud.getTerium().getConfigManager().getString("license")) != null) {
            Logger.licenseLog("License is accessible.", LogType.INFO);
            System.out.println(" ");
        } else {
            Logger.licenseLog("Your license isn't accessible.", LogType.ERROR);
            Logger.licenseLog("Please typ a accessible license. Or are you one of the illegal owners of the cloud? ;)", LogType.ERROR);
            TeriumCloud.getTerium().getConfigManager().getJson().addProperty("setup", true);
            TeriumCloud.getTerium().getConfigManager().save();
            Thread.sleep(5000);
            System.exit(0);
        }*/
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
            TeriumCloud.getTerium().shutdownCloud();
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