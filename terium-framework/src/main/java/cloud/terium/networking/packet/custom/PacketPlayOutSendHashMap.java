package cloud.terium.networking.packet.custom;

import cloud.terium.teriumapi.pipe.Packet;

import java.util.HashMap;

public record PacketPlayOutSendHashMap(HashMap<String, Object> hashMap) implements Packet {
}
