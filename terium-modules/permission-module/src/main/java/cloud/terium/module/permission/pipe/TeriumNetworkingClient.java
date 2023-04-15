package cloud.terium.module.permission.pipe;

import cloud.terium.module.permission.pipe.packet.PacketPlayOutGroupUpdate;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TeriumNetworkingClient {

    public TeriumNetworkingClient() {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().addHandler(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                if (packet instanceof PacketPlayOutGroupUpdate newPacket) {

                }
            }
        });
    }
}