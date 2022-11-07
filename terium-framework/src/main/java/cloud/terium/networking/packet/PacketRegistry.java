package cloud.terium.networking.packet;

import cloud.terium.networking.packets.*;
import cloud.terium.networking.packets.group.*;
import cloud.terium.networking.packets.service.*;
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
        registerPacket(PacketPlayOutCreateLobbyGroup.class, 6, PacketPlayOutCreateLobbyGroup::new);
        registerPacket(PacketPlayOutCreateServerGroup.class, 7, PacketPlayOutCreateServerGroup::new);
        registerPacket(PacketPlayOutCreateProxyGroup.class, 8, PacketPlayOutCreateProxyGroup::new);
        registerPacket(PacketPlayOutGroupDelete.class, 9, PacketPlayOutGroupDelete::new);

        // Templat  e
        registerPacket(PacketPlayOutTemplateCreate.class, 10, PacketPlayOutTemplateCreate::new);
        registerPacket(PacketPlayOutTemplateDelete.class, 11, PacketPlayOutTemplateDelete::new);

        // Console packets
        registerPacket(PacketPlayOutSendConsole.class, 12, PacketPlayOutSendConsole::new);
        registerPacket(PacketPlayOutExecuteConsole.class, 13, PacketPlayOutExecuteConsole::new);
        registerPacket(PacketPlayOutCreateService.class, 14, PacketPlayOutCreateService::new);

        // Service packets
        registerPacket(PacketPlayOutServiceStart.class, 15, PacketPlayOutServiceStart::new);
        registerPacket(PacketPlayOutServiceShutdown.class, 16, PacketPlayOutServiceShutdown::new);
        registerPacket(PacketPlayOutServiceAdd.class, 17, PacketPlayOutServiceAdd::new);
        registerPacket(PacketPlayOutServiceRemove.class, 18, PacketPlayOutServiceRemove::new);
        registerPacket(PacketPlayOutServiceShutdowned.class, 19, PacketPlayOutServiceShutdowned::new);
        registerPacket(PacketPlayOutSuccessfullServiceShutdown.class, 20, PacketPlayOutSuccessfullServiceShutdown::new);
        registerPacket(PacketPlayOutServiceMemoryUpdatePacket.class, 21, PacketPlayOutServiceMemoryUpdatePacket::new);
        registerPacket(PacketPlayOutServiceOnlinePlayersUpdatePacket.class, 22, PacketPlayOutServiceOnlinePlayersUpdatePacket::new);
        registerPacket(PacketPlayOutServiceChangeState.class, 23, PacketPlayOutServiceChangeState::new);
        registerPacket(PacketPlayOutServiceLock.class, 24, PacketPlayOutServiceLock::new);
        registerPacket(PacketPlayOutServiceUnlock.class, 25, PacketPlayOutServiceUnlock::new);
        registerPacket(PacketPlayOutUpdateService.class, 26, PacketPlayOutUpdateService::new);
        registerPacket(PacketPlayOutServiceRestart.class, 27, PacketPlayOutServiceRestart::new);

        // Util packets
        registerPacket(PacketPlayOutReloadConfig.class, 28, PacketPlayOutReloadConfig::new);
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
