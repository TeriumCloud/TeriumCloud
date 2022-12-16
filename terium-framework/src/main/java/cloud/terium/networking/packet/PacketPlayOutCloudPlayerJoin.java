package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;

import java.util.UUID;

public record PacketPlayOutCloudPlayerJoin(String username, UUID uniqueId) implements Packet {
}