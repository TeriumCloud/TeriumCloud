package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;

public record PacketPlayOutServiceStart(ICloudServiceGroup iCloudServiceGroup) implements Packet {
}