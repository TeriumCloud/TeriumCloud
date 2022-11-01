package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutCreateService extends Packet {

    private final String name;
    private final ICloudServiceGroup serviceGroup;
    private final ITemplate template;
    private final int port;
    private final int maxPlayers;
    private final int memory;
    private final int serviceId;
    private final CloudServiceType cloudServiceType;

    public PacketPlayOutCreateService(String name, ICloudServiceGroup serviceGroup, ITemplate template, int port, int maxPlayers, int memory, int serviceId, CloudServiceType cloudServiceType) {
        this.name = name;
        this.serviceGroup = serviceGroup;
        this.template = template;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.serviceId = serviceId;
        this.cloudServiceType = cloudServiceType;
    }

    public PacketPlayOutCreateService(ByteBuf byteBuf) {
        this.name = readString(byteBuf);
        this.serviceGroup = TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(readString(byteBuf));
        this.template = TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(readString(byteBuf));
        this.port = byteBuf.readInt();
        this.maxPlayers = byteBuf.readInt();
        this.memory = byteBuf.readInt();
        this.serviceId = byteBuf.readInt();
        this.cloudServiceType = CloudServiceType.valueOf(readString(byteBuf));
    }

    public String name() {
        return name;
    }

    public ICloudServiceGroup serviceGroup() {
        return serviceGroup;
    }

    public ITemplate template() {
        return template;
    }

    public int port() {
        return port;
    }

    public int maxPlayers() {
        return maxPlayers;
    }

    public int memory() {
        return memory;
    }

    public int serviceId() {
        return serviceId;
    }

    public CloudServiceType cloudServiceType() {
        return cloudServiceType;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
        writeString(serviceGroup.getServiceGroupName(), byteBuf);
        writeString(template.getName(), byteBuf);
        byteBuf.writeInt(port);
        byteBuf.writeInt(maxPlayers);
        byteBuf.writeInt(memory);
        byteBuf.writeInt(serviceId);
        writeString(cloudServiceType.name(), byteBuf);
    }
}