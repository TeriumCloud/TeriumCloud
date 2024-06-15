package cloud.terium.module.dockerizedservices;

import cloud.terium.cloudsystem.common.utils.logger.Logger;
import cloud.terium.module.dockerizedservices.config.ConfigLoader;
import cloud.terium.module.dockerizedservices.config.DockerizedConfig;
import cloud.terium.module.dockerizedservices.console.DockerizedServicesCommand;
import cloud.terium.module.dockerizedservices.service.DockerizedServiceListener;
import cloud.terium.module.dockerizedservices.service.DockerizedServiceFactory;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.module.annotation.Module;
import lombok.Getter;

@Module(name = "dockerized-services-module", author = "Jxnnik(ByRaudy)", version = "1.8-OXYGEN", description = "", reloadable = true, moduleType = ModuleType.CLOUD_ONLY)
@Getter
public class TeriumDockerizedServices implements IModule {

    private static TeriumDockerizedServices instance;
    private DockerizedServiceFactory serviceFactory;
    private ConfigLoader configLoader;
    private DockerizedConfig dockerizedConfig;

    public static TeriumDockerizedServices getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        serviceFactory = new DockerizedServiceFactory();
        configLoader = new ConfigLoader();
        dockerizedConfig = new DockerizedConfig();

        serviceFactory.startKeepAliveCheckForServices();
        TeriumAPI.getTeriumAPI().getFactory().getCommandFactory().registerCommand(new DockerizedServicesCommand());
        TeriumAPI.getTeriumAPI().getProvider().getEventProvider().subscribeListener(new DockerizedServiceListener());
        configLoader.getIncludedGroupsLoader().getJson().keySet().stream().filter(s -> !s.equals("exampleGroup")).forEach(s -> {
            TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().unbindServiceGroup(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(s).orElseGet(null));
            serviceFactory.bindServiceGroup(TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(s).orElseGet(null));
        });
    }

    @Override
    public void onDisable() {
    }

    public void reload() {
    }
}
