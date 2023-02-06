package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

public record PacketPlayOutServiceStart(ICloudService cloudService, INode node) implements Packet {

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

            @Override
            public HashMap<String, Object> getPropertyMap() {
                return cloudService.getPropertyMap();
            }
        };
    }

    @Override
    public INode node() {
        return new INode() {
            @Override
            public String getName() {
                return node.getName();
            }

            @Override
            public String getKey() {
                return node.getKey();
            }

            @Override
            public InetSocketAddress getAddress() {
                return node.getAddress();
            }

            @Override
            public boolean isConnected() {
                return node.isConnected();
            }

            @Override
            public long getUsedMemory() {
                return node.getUsedMemory();
            }

            @Override
            public void setUsedMemory(long usedMemory) {
                node.setUsedMemory(usedMemory);
            }

            @Override
            public long getMaxMemory() {
                return node.getMaxMemory();
            }

            @Override
            public void setMaxMemory(long maxMemory) {
                node.setMaxMemory(maxMemory);
            }

            @Override
            public void update() {
                node.update();
            }

            @Override
            public void disconnect() {
                node.disconnect();
            }

            @Override
            public void stop() {
                node.stop();
            }
        };
    }
}