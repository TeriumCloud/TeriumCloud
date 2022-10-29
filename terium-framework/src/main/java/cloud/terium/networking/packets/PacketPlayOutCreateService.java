package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutCreateService extends Packet {

    private final String name;
    private final ICloudServiceGroup serviceGroup;
    private final ITemplate template;
    private final int port;
    private final int maxPlayers;

    public PacketPlayOutCreateService(String name, ICloudServiceGroup serviceGroup, ITemplate template, int port, int maxPlayers) {
        this.name = name;
        this.serviceGroup = serviceGroup;
        this.template = template;
        this.port = port;
        this.maxPlayers = maxPlayers;
    }

    public PacketPlayOutCreateService(ByteBuf byteBuf) {
        this.name = readString(byteBuf);
        this.serviceGroup = TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(readString(byteBuf));
        this.template = TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(readString(byteBuf));
        this.port = byteBuf.readInt();
        this.maxPlayers = byteBuf.readInt();
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

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
        writeString(serviceGroup.getServiceGroupName(), byteBuf);
        writeString(template.name(), byteBuf);
        byteBuf.writeInt(port);
        byteBuf.writeInt(maxPlayers);
    }
}