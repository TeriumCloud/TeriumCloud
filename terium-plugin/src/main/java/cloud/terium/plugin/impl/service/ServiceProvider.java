package cloud.terium.plugin.impl.service;

import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceProvider;

import java.util.List;
import java.util.Optional;

public class ServiceProvider implements ICloudServiceProvider {
    @Override
    public Optional<ICloudService> getCloudServiceByName(String serviceName) {
        return Optional.empty();
    }

    @Override
    public List<ICloudService> getCloudServicesByGroupName(String serviceGroup) {
        return null;
    }

    @Override
    public List<ICloudService> getCloudServicesByGroupTitle(String groupTitle) {
        return null;
    }

    @Override
    public List<ICloudService> getAllLobbyServices() {
        return null;
    }

    @Override
    public List<ICloudService> getAllCloudServices() {
        return null;
    }
}
