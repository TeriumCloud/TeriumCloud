package cloud.terium.networking.packet;

import cloud.terium.networking.packets.*;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.function.Function;

public class PacketRegistry {

    private final HashMap<Integer, Function<ByteBuf, Packet>> packets;
    private final HashMap<Class<Packet>, Integer> packetIds;

    public PacketRegistry() {
        this.packetIds = new HashMap<>();
        this.packets = new HashMap<>();

        // Player packets
        registerPacket(PacketPlayOutCloudPlayerConnect.class, 0, PacketPlayOutCloudPlayerConnect::new);
        registerPacket(PacketPlayOutCloudPlayerJoin.class, 1, PacketPlayOutCloudPlayerJoin::new);
        registerPacket(PacketPlayOutCloudPlayerQuit.class, 2, PacketPlayOutCloudPlayerQuit::new);

        // Group packets
        registerPacket(PacketPlayOutGroupsReload.class, 3, PacketPlayOutGroupsReload::new);
        registerPacket(PacketPlayOutGroupReload.class, 4, PacketPlayOutGroupReload::new);

        // Service packets
        registerPacket(PacketPlayOutServiceStart.class, 5, PacketPlayOutServiceStart::new);
        registerPacket(PacketPlayOutServiceShutdown.class, 6, PacketPlayOutServiceShutdown::new);
        registerPacket(PacketPlayOutServiceOnline.class, 7, PacketPlayOutServiceOnline::new);
        registerPacket(PacketPlayOutServiceAdd.class, 8, PacketPlayOutServiceAdd::new);
        registerPacket(PacketPlayOutServiceRemove.class, 9, PacketPlayOutServiceRemove::new);
        registerPacket(PacketPlayOutServiceForceShutdown.class, 10, PacketPlayOutServiceForceShutdown::new);

        // Util packets
        registerPacket(PacketPlayOutReloadConfig.class, 11, PacketPlayOutReloadConfig::new);
    }

    private <T extends Packet> void registerPacket(Class<T> packetClass, int packetId, Function<ByteBuf, Packet> function) {
        packets.put(packetId, function);
        packetIds.put((Class<Packet>) packetClass, packetId);
    }

    public boolean containsPacketId(int packetId) {
        return packets.containsKey(packetId);
    }

    public Packet readPacket(int id, ByteBuf byteBuf) {
        Function<ByteBuf, Packet> packetFunction = this.packets.get(id);
        if (packetFunction == null)
            throw new IllegalStateException("Packet with id #" + id + " not registered.");
        return packetFunction.apply(byteBuf);
    }

    public int getIdByPacketClass(Class<Packet> packetClass) {
        if (packetIds.containsKey(packetClass))
            return packetIds.get(packetClass);

        throw new IllegalStateException("Packet no registered.");
    }
}
