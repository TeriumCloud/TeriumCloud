package cloud.terium.networking.packet.codec;

import cloud.terium.networking.packet.Packet;
import cloud.terium.networking.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    private final PacketRegistry packetLibary;

    public PacketDecoder(PacketRegistry packetLibary) {
        this.packetLibary = packetLibary;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {
        if (!ctx.channel().isActive()) {
            input.skipBytes(input.readableBytes());
            return;
        }
        int packetId = input.readInt();
        if (!packetLibary.containsPacketId(packetId)) throw new DecoderException("Ungültige Packet-ID: $packetId");

        Packet packet = packetLibary.readPacket(packetId, input);
        output.add(packet);
    }
}
