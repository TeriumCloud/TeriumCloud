package cloud.terium.networking.packet.player;

import cloud.terium.teriumapi.pipe.Packet;

import java.util.UUID;

public record PacketPlayOutCloudPlayerDisconnect(UUID cloudPlayer, String message) implements Packet {
}