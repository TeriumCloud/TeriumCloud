package cloud.terium.networking.packets.group;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.template.ITemplate;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class PacketPlayOutCreateProxyGroup extends Packet {

    private final String name;
    private final String groupTitle;
    private final String node;
    private final String template;
    private final String version;
    private final boolean maintenance;
    private final int port;
    private final int maximumPlayers;
    private final int memory;
    private final int minimalServices;
    private final int maximalServices;

    public PacketPlayOutCreateProxyGroup(String name, String groupTitle, String node, ITemplate template, String version, boolean maintenance, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.node = node;
        this.template = template.getName();
        this.version = version;
        this.maintenance = maintenance;
        this.port = port;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
    }

    public PacketPlayOutCreateProxyGroup(ByteBuf byteBuf) {
        this.name = readString(byteBuf);
        this.groupTitle = readString(byteBuf);
        this.node = readString(byteBuf);
        this.template = readString(byteBuf);
        this.version = readString(byteBuf);
        this.maintenance = byteBuf.readBoolean();
        this.port = byteBuf.readInt();
        this.maximumPlayers = byteBuf.readInt();
        this.memory = byteBuf.readInt();
        this.minimalServices = byteBuf.readInt();
        this.maximalServices = byteBuf.readInt();
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
        writeString(groupTitle, byteBuf);
        writeString(node, byteBuf);
        writeString(template, byteBuf);
        writeString(version, byteBuf);
        byteBuf.writeBoolean(maintenance);
        byteBuf.writeInt(port);
        byteBuf.writeInt(maximumPlayers);
        byteBuf.writeInt(memory);
        byteBuf.writeInt(minimalServices);
        byteBuf.writeInt(maximalServices);
    }
}