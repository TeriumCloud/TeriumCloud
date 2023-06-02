package cloud.terium.module.privateserver;

import cloud.terium.module.privateserver.config.Config;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ServiceGroupBuilder;
import lombok.Getter;

@Getter
public final class PrivateServerModule implements IModule {

    private static PrivateServerModule instance;
    private Config config;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config();

        if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null)
            TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(config.getJson().get("config").getAsJsonObject().get("service-group").getAsString()).ifPresentOrElse(serviceGroup -> {
            }, () -> {
                ICloudServiceGroup cloudServiceGroup = new ServiceGroupBuilder(config.getJson().get("config").getAsJsonObject().get("service-group").getAsString(), ServiceType.Server)
                        .setGroupTitle("Service group for private-servers")
                        .setMaintenance(false)
                        .setMemory(1024)
                        .setMinimalServices(0)
                        .setMaximalServices(10)
                        .build();
                TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Created service-group for private servers. (%group%)".replace("%group%", cloudServiceGroup.getGroupName()), LogType.INFO);
            });
    }

    @Override
    public void onDisable() {

    }

    public static PrivateServerModule getInstance() {
        return instance;
    }
}