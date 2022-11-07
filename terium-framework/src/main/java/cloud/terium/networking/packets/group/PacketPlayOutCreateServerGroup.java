package cloud.terium.networking.packets.group;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.template.ITemplate;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class PacketPlayOutCreateServerGroup extends Packet {

    private String name;
    private String groupTitle;
    private String node;
    private String template;
    private String version;
    private int maximumPlayers;
    private boolean maintenance;
    private int memory;
    private int minimalServices;
    private int maximalServices;

    public PacketPlayOutCreateServerGroup(String name, String groupTitle, String node, ITemplate template, String version, boolean maintenance, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.node = node;
        this.template = template.getName();
        this.version = version;
        this.maintenance = maintenance;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
    }

    public PacketPlayOutCreateServerGroup(ByteBuf byteBuf) {
        this.name = readString(byteBuf);
        this.groupTitle = readString(byteBuf);
        this.node = readString(byteBuf);
        this.template = readString(byteBuf);
        this.version = readString(byteBuf);
        this.maintenance = byteBuf.readBoolean();
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
        byteBuf.writeInt(maximumPlayers);
        byteBuf.writeInt(memory);
        byteBuf.writeInt(minimalServices);
        byteBuf.writeInt(maximalServices);
    }
}