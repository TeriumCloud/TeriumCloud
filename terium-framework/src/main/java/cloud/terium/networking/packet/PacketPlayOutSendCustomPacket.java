package cloud.terium.networking.packet;

import cloud.terium.teriumapi.pipe.CustomPacket;
import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendCustomPacket(CustomPacket packet) implements Packet {
}
