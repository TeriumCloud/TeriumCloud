package cloud.terium.cloudsystem.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ServiceGroupBuilder;

public class CloudGroupBuilder extends ServiceGroupBuilder {

    public CloudGroupBuilder(String name, ServiceType serviceType) {
        super(name, serviceType);
    }

    @Override
    public ICloudServiceGroup build() {
        Logger.log("called", LogType.INFO);
        if(getName() == null)
            throw new NullPointerException("name cannot be null");
        if(getServiceType() == null)
            throw new NullPointerException("cloud service type cannot be null");

        ICloudServiceGroup cloudServiceGroup = null;
        switch (getServiceType()) {
            case Proxy -> TeriumCloud.getTerium().getServiceGroupFactory().createProxyGroup(getName(), getGroupTitle(), getNode(), getFallbackNodes(), getTemplates(), getVersion(), isMaintenance(), isStatic(), getPort(), getMaximumPlayers(), getMemory(), getMinimalServices(), getMaximalServices());
            case Lobby -> TeriumCloud.getTerium().getServiceGroupFactory().createLobbyGroup(getName(), getGroupTitle(), getNode(), getFallbackNodes(), getTemplates(), getVersion(), isMaintenance(), isStatic(), getMaximumPlayers(), getMemory(), getMinimalServices(), getMaximalServices());
            case Server -> TeriumCloud.getTerium().getServiceGroupFactory().createServerGroup(getName(), getGroupTitle(), getNode(), getFallbackNodes(), getTemplates(), getVersion(), isMaintenance(), isStatic(), getMaximumPlayers(), getMemory(), getMinimalServices(), getMaximalServices());
        }
        return cloudServiceGroup;
    }
}