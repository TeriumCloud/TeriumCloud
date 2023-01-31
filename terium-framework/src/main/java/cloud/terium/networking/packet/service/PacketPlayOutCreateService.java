package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record PacketPlayOutCreateService(String name, ICloudServiceGroup serviceGroup, List<ITemplate> templates,
                                         int maxPlayers, int memory, int serviceId) implements Packet {

    @Override
    public ICloudServiceGroup serviceGroup() {
        return new ICloudServiceGroup() {
            @Override
            public String getGroupName() {
                return serviceGroup.getGroupName();
            }

            @Override
            public String getGroupTitle() {
                return serviceGroup.getGroupTitle();
            }

            @Override
            public INode getGroupNode() {
                return serviceGroup.getGroupNode();
            }

            @Override
            public List<INode> getGroupFallbackNode() {
                return serviceGroup.getGroupFallbackNode();
            }

            @Override
            public List<ITemplate> getTemplates() {
                return serviceGroup.getTemplates();
            }

            @Override
            public ServiceType getServiceType() {
                return serviceGroup.getServiceType();
            }

            @Override
            public String getVersion() {
                return serviceGroup.getVersion();
            }

            @Override
            public void setVersion(String version) {
                serviceGroup.setVersion(version);
            }

            @Override
            public boolean isMaintenance() {
                return serviceGroup.isMaintenance();
            }

            @Override
            public void setMaintenance(boolean maintenance) {
                serviceGroup.setMaintenance(maintenance);
            }

            @Override
            public boolean isStatic() {
                return serviceGroup.isStatic();
            }

            @Override
            public void setStatic(boolean isStatic) {
                serviceGroup.setStatic(isStatic);
            }

            @Override
            public boolean hasPort() {
                return serviceGroup.hasPort();
            }

            @Override
            public int getPort() {
                return serviceGroup.getPort();
            }

            @Override
            public int getMaxPlayers() {
                return serviceGroup.getMaxPlayers();
            }

            @Override
            public int getMemory() {
                return serviceGroup.getMemory();
            }

            @Override
            public void setMemory(int memory) {
                serviceGroup.setMemory(memory);
            }

            @Override
            public int getMinServices() {
                return serviceGroup.getMinServices();
            }

            @Override
            public void setMinServices(int services) {
                serviceGroup.setMinServices(services);
            }

            @Override
            public int getMaxServices() {
                return serviceGroup.getMaxServices();
            }

            @Override
            public void setMaxServices(int services) {
                serviceGroup.setMaxServices(services);
            }

            @Override
            public void setMaxPlayer(int players) {
                serviceGroup.setMaxPlayer(players);
            }

            @Override
            public void update() {
                serviceGroup.update();
            }
        };
    }

    @Override
    public List<ITemplate> templates() {
        List<ITemplate> templateList = new ArrayList<>();
        templates.forEach(template -> {
            templateList.add(new ITemplate() {
                @Override
                public String getName() {
                    return template.getName();
                }

                @Override
                public Path getPath() {
                    return template.getPath();
                }
            });
        });
        return templateList;
    }
}