package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.player.ICloudPlayer;

public record PacketPlayOutCloudPlayerJoin(ICloudPlayer cloudPlayer) implements Packet {
}