package cloud.terium.teriumapi.pipe.packets;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendString(String string) implements Packet {
}
