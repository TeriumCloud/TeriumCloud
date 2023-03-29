package cloud.terium.networking.packet;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutCheckVersion(String version) implements Packet {
}