package cloud.terium.plugin.impl.pipe;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.service.impl.CloudService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;

public class DefaultTeriumNetworking implements IDefaultTeriumNetworking {

    private final TeriumClient teriumClient;

    @SneakyThrows
    public DefaultTeriumNetworking() {
        teriumClient = new TeriumClient(System.getProperty("netty-address"), Integer.parseInt(System.getProperty("netty-port")));
        getChannel().pipeline().addLast(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet) {
                System.out.println("packet?");
                if(packet instanceof PacketPlayOutCreateService newPacket)
                    TeriumPlugin.getInstance().getServiceProvider().getAllCloudServices().add(new CloudService(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.parsedNode().orElseGet(null), newPacket.parsedServiceGroup().orElseGet(null), newPacket.parsedTemplates()));
            }
        });
    }

    @Override
    public Channel getChannel() {
        return teriumClient.getChannel();
    }

    public void addHandler(SimpleChannelInboundHandler<Packet> handler) {
        getChannel().pipeline().addLast(handler);
    }

    @Override
    public void sendPacket(Packet packet) {
        getChannel().writeAndFlush(packet);
    }
}
