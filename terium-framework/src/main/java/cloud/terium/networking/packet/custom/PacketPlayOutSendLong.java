package cloud.terium.networking.packet.custom;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendLong(long integer) implements Packet {
}
