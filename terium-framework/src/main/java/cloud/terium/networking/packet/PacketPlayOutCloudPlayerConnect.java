package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;

public record PacketPlayOutCloudPlayerConnect(ICloudPlayer cloudPlayer, ICloudService cloudService) implements Packet {
}