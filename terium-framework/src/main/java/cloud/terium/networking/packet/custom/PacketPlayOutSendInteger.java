package cloud.terium.networking.packet.custom;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendInteger(int integer) implements Packet {
}
