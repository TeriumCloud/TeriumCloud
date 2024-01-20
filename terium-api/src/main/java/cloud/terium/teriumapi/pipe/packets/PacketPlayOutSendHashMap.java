package cloud.terium.teriumapi.pipe.packets;

import cloud.terium.teriumapi.pipe.Packet;

import java.util.HashMap;

public record PacketPlayOutSendHashMap(HashMap<String, Object> hashMap) implements Packet {
}
