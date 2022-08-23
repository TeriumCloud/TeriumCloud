package cloud.terium.networking.packet.codec;

import cloud.terium.networking.packet.Packet;
import cloud.terium.networking.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private final PacketRegistry packetLibary;

    public PacketEncoder(PacketRegistry packetLibary) {
        this.packetLibary = packetLibary;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        int packetId = packetLibary.getIdByPacketClass((Class<Packet>) packet.getClass());
        byteBuf.writeInt(packetId);
        packet.write(byteBuf);
    }
}
