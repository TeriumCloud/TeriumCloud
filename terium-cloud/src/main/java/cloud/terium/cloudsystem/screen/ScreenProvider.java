package cloud.terium.cloudsystem.screen;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceProvider {

    private final HashMap<ICloudService, List<String>> cachedScreens;
    private ICloudService currentScreen;

    public ServiceProvider() {
        this.cachedScreens = new HashMap<>();
        this.currentScreen = null;
    }

    public void setCurrentScreen(ICloudService iCloudService) {
        this.currentScreen = iCloudService;
    }

    public ICloudService getCurrentScreen() {
        return currentScreen;
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