package cloud.terium.networking.packet.player;

import cloud.terium.teriumapi.pipe.Packet;

import java.net.InetSocketAddress;
import java.util.UUID;

public record PacketPlayOutCloudPlayerRegister(String username, UUID uniquedId, InetSocketAddress address, String value,
                                               String signature, String cloudService) implements Packet {
}