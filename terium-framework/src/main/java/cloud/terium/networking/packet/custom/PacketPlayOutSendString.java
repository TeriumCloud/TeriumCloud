package cloud.terium.networking.packet.custom;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendString(String string) implements Packet {
}
