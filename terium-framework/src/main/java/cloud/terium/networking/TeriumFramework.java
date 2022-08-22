package cloud.terium.networking;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.protocol.PacketRegistry;
import lombok.SneakyThrows;

public class TeriumFramework {

    private static final PacketRegistry packetLibary = new PacketRegistry();

    public static PacketRegistry getPacketLibary() {
        return packetLibary;
    }

    @SneakyThrows
    public static TeriumClient createClient(String host, int port) {
        return new TeriumClient(host, port);
    }
}
