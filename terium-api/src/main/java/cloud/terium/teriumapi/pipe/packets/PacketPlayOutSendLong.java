package cloud.terium.teriumapi.pipe.packets;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendLong(long integer) implements Packet {
}
