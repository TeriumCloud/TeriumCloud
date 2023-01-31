package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public record PacketPlayOutSuccessfullyServiceStarted(ICloudService cloudService) implements Packet {

    @Override
    public ICloudService cloudService() {
        return new ICloudService() {
            @Override
            public String getServiceName() {
                return cloudService.getServiceName();
            }

            @Override
            public int getServiceId() {
                return cloudService.getServiceId();
            }

            @Override
            public int getPort() {
                return cloudService.getPort();
            }

            @Override
            public List<ITemplate> getTemplates() {
                return cloudService.getTemplates();
            }

            @Override
            public int getOnlinePlayers() {
                return cloudService.getOnlinePlayers();
            }

            @Override
            public void setOnlinePlayers(int onlinePlayers) {
                cloudService.setOnlinePlayers(onlinePlayers);
            }

            @Override
            public long getUsedMemory() {
                return cloudService.getUsedMemory();
            }

            @Override
            public void setUsedMemory(long usedMemory) {
                cloudService.setUsedMemory(usedMemory);
            }

            @Override
            public void update() {
                cloudService.update();
            }

            @Override
            public ICloudServiceGroup getServiceGroup() {
                return cloudService.getServiceGroup();
            }

            @Override
            public INode getServiceNode() {
                return cloudService.getServiceNode();
            }

            @Override
            public ServiceState getServiceState() {
                return cloudService.getServiceState();
            }

            @Override
            public void setServiceState(ServiceState serviceState) {
                cloudService.setServiceState(serviceState);
            }

            @Override
            public boolean isLocked() {
                return cloudService.isLocked();
            }

            @Override
            public void setLocked(boolean locked) {
                cloudService.setLocked(locked);
            }

            @Override
            public void addProperty(String name, Object property) {
                cloudService.addProperty(name, property);
            }

            @Override
            public void removeProperty(String name) {
                cloudService.removeProperty(name);
            }

            @Override
            public Object getProperty(String name) {
                return cloudService.getProperty(name);
            }
        };
    }
}