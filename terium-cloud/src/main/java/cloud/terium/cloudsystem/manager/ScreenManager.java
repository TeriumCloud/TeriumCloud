package cloud.terium.cloudsystem.manager;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.HashMap;
import java.util.List;

public class ScreenManager {

    private final HashMap<ICloudService, List<String>> cachedScreens;

    public ScreenManager() {
        this.cachedScreens = new HashMap<>();
    }

    public void addLogToScreen(ICloudService iCloudService, String log) {
        cachedScreens.get(iCloudService).add(log);
    }

    public List<String> getLogsFromService(ICloudService iCloudService) {
        return cachedScreens.get(iCloudService);
    }
}
