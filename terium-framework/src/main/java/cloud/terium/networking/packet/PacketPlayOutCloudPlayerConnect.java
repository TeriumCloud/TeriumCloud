package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;

import java.util.UUID;

public record PacketPlayOutCloudPlayerConnect(UUID uniqueId, String service) implements Packet {
}