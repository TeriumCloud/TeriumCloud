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

    public void setCurrentScreen(ICloudService cloudService) {
        this.currentScreen = cloudService;
    }

    public void addCloudService(ICloudService cloudService) {
        cachedScreens.put(cloudService, new ArrayList<>());
    }

    public void removeCloudService(ICloudService cloudService) {
        cachedScreens.remove(cloudService);
    }

    public void addLogToScreen(ICloudService cloudService, String log) {
        cachedScreens.get(cloudService).add(log);
    }

    public List<String> getLogsFromService(ICloudService cloudService) {
        return cachedScreens.get(cloudService);
    }
}