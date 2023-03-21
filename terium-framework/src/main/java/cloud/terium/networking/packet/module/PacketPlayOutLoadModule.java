package cloud.terium.networking.packet.module;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutLoadModule(String fileName) implements Packet {
}
