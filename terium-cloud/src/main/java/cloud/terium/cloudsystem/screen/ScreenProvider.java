package cloud.terium.cloudsystem.screen;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenProvider {

    private final HashMap<ICloudService, List<String>> cachedScreens;
    private ICloudService currentScreen;

    public ScreenProvider() {
        this.cachedScreens = new HashMap<>();
        this.currentScreen = null;
    }

    public ICloudService getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(ICloudService iCloudService) {
        this.currentScreen = iCloudService;
    }

    public void addCloudService(ICloudService iCloudService) {
        cachedScreens.put(iCloudService, new ArrayList<>());
    }

    public void removeCloudService(ICloudService iCloudService) {
        cachedScreens.remove(iCloudService);
    }

    public void addLogToScreen(ICloudService iCloudService, String log) {
        cachedScreens.get(iCloudService).add(log);
    }

    public List<String> getLogsFromService(ICloudService iCloudService) {
        return cachedScreens.get(iCloudService);
    }
}