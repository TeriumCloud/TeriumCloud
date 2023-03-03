package cloud.terium.plugin.velocity.terium;

import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerConnect;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRemove;
import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.Packet;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class NetworkingAdapter {

    public NetworkingAdapter() {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().addHandler(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                if (packet instanceof PacketPlayOutSuccessfullyServiceStarted packetAdd) {
                    System.out.println("packet");
                    if (TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetAdd.serviceName()).isPresent()) {
                        return;
                    }

                    TeriumVelocityStartup.getInstance().getProxyServer().registerServer(new ServerInfo(packetAdd.serviceName(), new InetSocketAddress("127.0.0.1", packetAdd.parsedCloudService().orElseGet(null).getPort())));
                    System.out.println("register");
                }

                if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                    System.out.println("packet 2");
                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(packetRemove.serviceName()).ifPresentOrElse(cloudService -> {
                        TeriumVelocityStartup.getInstance().getProxyServer().unregisterServer(TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetRemove.serviceName()).orElse(null).getServerInfo());
                    }, () -> {
                        System.out.println("This service isn't connected with the proxy service.");
                    });
                }

                if (packet instanceof PacketPlayOutCloudPlayerConnect packetConnect) {
                    TeriumVelocityStartup.getInstance().getProxyServer().getPlayer(packetConnect.cloudPlayer()).ifPresent(player -> player.createConnectionRequest(TeriumVelocityStartup.getInstance().getProxyServer().getServer(packetConnect.cloudService()).orElse(null)).connect());
                }
            }
        });
    }
}
