package cloud.terium.teriumapi.pipe.packets;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendInteger(int integer) implements Packet {
}
