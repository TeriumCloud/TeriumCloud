package cloud.terium.cloudsystem.manager;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenManager {

    private final HashMap<ICloudService, List<String>> cachedScreens;
    private final List<String> cloudLogs;
    private ICloudService currentScreen;

    public ScreenManager() {
        this.cachedScreens = new HashMap<>();
        this.cloudLogs = new ArrayList<>();
        this.currentScreen = null;
    }

    public void setCurrentScreen(ICloudService iCloudService) {
        this.currentScreen = iCloudService;
    }

    public ICloudService getCurrentScreen() {
        return currentScreen;
    }

    public List<String> getCloudLogs() {
        return cloudLogs;
    }

    public void addCloudService(ICloudService iCloudService) {
        cachedScreens.put(iCloudService, new ArrayList<>());
    }

    public void addLogToScreen(ICloudService iCloudService, String log) {
        cachedScreens.get(iCloudService).add(log);
    }

    public List<String> getLogsFromService(ICloudService iCloudService) {
        return cachedScreens.get(iCloudService);
    }
}