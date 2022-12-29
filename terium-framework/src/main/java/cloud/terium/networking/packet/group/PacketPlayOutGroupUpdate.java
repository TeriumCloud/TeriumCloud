package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public record PacketPlayOutGroupUpdate(ICloudServiceGroup serviceGroup) implements Packet {
}