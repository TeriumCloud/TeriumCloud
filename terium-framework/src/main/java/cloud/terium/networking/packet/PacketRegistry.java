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
        registerPacket(PacketPlayOutCloudPlayerConnectedService.class, 1, PacketPlayOutCloudPlayerConnectedService::new);
        registerPacket(PacketPlayOutCloudPlayerJoin.class, 2, PacketPlayOutCloudPlayerJoin::new);
        registerPacket(PacketPlayOutCloudPlayerQuit.class, 3, PacketPlayOutCloudPlayerQuit::new);

        // Group packets
        registerPacket(PacketPlayOutGroupsReload.class, 4, PacketPlayOutGroupsReload::new);
        registerPacket(PacketPlayOutGroupReload.class, 5, PacketPlayOutGroupReload::new);

        // Service packets
        registerPacket(PacketPlayOutServiceStart.class, 6, PacketPlayOutServiceStart::new);
        registerPacket(PacketPlayOutServiceShutdown.class, 7, PacketPlayOutServiceShutdown::new);
        registerPacket(PacketPlayOutServiceAdd.class, 8, PacketPlayOutServiceAdd::new);
        registerPacket(PacketPlayOutServiceRemove.class, 9, PacketPlayOutServiceRemove::new);
        registerPacket(PacketPlayOutServiceShutdowned.class, 10, PacketPlayOutServiceShutdowned::new);
        registerPacket(PacketPlayOutSuccessfullServiceShutdown.class, 11, PacketPlayOutSuccessfullServiceShutdown::new);
        registerPacket(PacketPlayOutServiceMemoryUpdatePacket.class, 12, PacketPlayOutServiceMemoryUpdatePacket::new);
        registerPacket(PacketPlayOutServiceOnlinePlayersUpdatePacket.class, 13, PacketPlayOutServiceOnlinePlayersUpdatePacket::new);
        registerPacket(PacketPlayOutServiceChangeState.class, 14, PacketPlayOutServiceChangeState::new);
        registerPacket(PacketPlayOutServiceLock.class, 15, PacketPlayOutServiceLock::new);
        registerPacket(PacketPlayOutServiceUnlock.class, 16, PacketPlayOutServiceUnlock::new);

        // Util packets
        registerPacket(PacketPlayOutReloadConfig.class, 17, PacketPlayOutReloadConfig::new);
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
