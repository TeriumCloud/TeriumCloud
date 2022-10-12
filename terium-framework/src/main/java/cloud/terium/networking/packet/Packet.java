package cloud.terium.networking.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet implements cloud.terium.teriumapi.network.Packet {
    public abstract void write(ByteBuf byteBuf);

    public void writeString(String string, ByteBuf byteBuf) {
        byteBuf.writeInt(string.length());
        byteBuf.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public String readString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        return byteBuf.readCharSequence(length, StandardCharsets.UTF_8).toString();
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
