package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.player.ICloudPlayer;

import java.util.UUID;

public record PacketPlayOutCloudPlayerJoin(ICloudPlayer cloudPlayer) implements Packet {
}