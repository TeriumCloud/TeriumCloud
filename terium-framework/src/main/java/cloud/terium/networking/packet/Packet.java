package cloud.terium.networking.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet implements cloud.terium.teriumapi.network.Packet {
    public abstract void write(ByteBuf byteBuf);

    public void writeString(String string, ByteBuf byteBuf) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public String readString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void writeStringArray(List<String> list, ByteBuf byteBuf) {
        byteBuf.writeInt(list.size());
        list.forEach(string -> writeString(string, byteBuf));
    }

    public List<String> readStringArray(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            list.add(readString(byteBuf));
        }

        return list;
    }
}
