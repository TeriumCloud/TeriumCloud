package cloud.terium.bridge.networking;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.BridgeBukkitStartup;
import cloud.terium.bridge.player.CloudRank;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.cloudsystem.networking.DefaultTeriumNetworking;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.service.ServiceType;
import cloud.terium.networking.packet.Packet;
import cloud.terium.networking.packets.*;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.File;
import java.net.InetSocketAddress;

@Getter
public final class TeriumNetworkListener {

    private final DefaultTeriumNetworking defaultTeriumNetworking;

    public TeriumNetworkListener(DefaultTeriumNetworking defaultTeriumNetworking) {
        this.defaultTeriumNetworking = defaultTeriumNetworking;

        defaultTeriumNetworking.getClient().getChannel().pipeline().addLast(new SimpleChannelInboundHandler<Packet>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
                System.out.println(packet.getClass().getSimpleName());
                if (packet instanceof PacketPlayOutServiceOnline packetOnline) {
                    TeriumBridge.getInstance().getServiceManager().getServiceByName(packetOnline.minecraftService()).online(packetOnline.online());
                }

                if(packet instanceof PacketPlayOutGroupsReload) {
                    TeriumBridge.getInstance().getServiceGroupManager().reloadGroups(true);
                }

                if(packet instanceof PacketPlayOutGroupReload groupReload) {
                    TeriumBridge.getInstance().getServiceGroupManager().reloadGroup(new File("../../groups/" + TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(groupReload.serviceGroup()).serviceType() + "/" + groupReload.serviceGroup() + ".json"));
                }

                if (packet instanceof PacketPlayOutServiceForceShutdown packetPlayOutServiceShutdown) {
                    if(TeriumBridge.getInstance().getThisService().serviceType().equals(ServiceType.Proxy)) {
                        BridgeVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId()).getRank().equals(CloudRank.Admin)).forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The service <dark_gray>'<#37b4b4>" + packetPlayOutServiceShutdown.serviceName() + "<dark_gray>' <white>is shutting down...")));
                    }

                    if(TeriumBridge.getInstance().getThisName().equals(packetPlayOutServiceShutdown.serviceName())) {
                        if (TeriumBridge.getInstance().getThisService().serviceType().equals(ServiceType.Proxy)) BridgeVelocityStartup.getInstance().getProxyServer().shutdown(); else BridgeBukkitStartup.getInstance().getServer().shutdown();
                    }
                }

                if (TeriumBridge.getInstance().getThisService().serviceType().equals(ServiceType.Proxy)) {
                    if (packet instanceof PacketPlayOutServiceAdd packetAdd) {
                        TeriumBridge.getInstance().getServiceManager().addService(new MinecraftService(TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(packetAdd.serviceGroup()), packetAdd.serviceId(), packetAdd.port()), true);
                        if (BridgeVelocityStartup.getInstance().getProxyServer().getServer(packetAdd.serviceName()).isPresent()) {
                            return;
                        }

                        BridgeVelocityStartup.getInstance().getProxyServer().registerServer(new ServerInfo(packetAdd.serviceName(), new InetSocketAddress("127.0.0.1", packetAdd.port())));
                        BridgeVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId()).getRank().equals(CloudRank.Admin)).forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The service <dark_gray>'<#37b4b4>" + packetAdd.serviceName() + "<dark_gray>' <white>is starting...")));
                    }

                    if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                        if (TeriumBridge.getInstance().getServiceManager().getServiceByName(packetRemove.serviceName()) != null) {
                            TeriumBridge.getInstance().getServiceManager().removeService(TeriumBridge.getInstance().getServiceManager().getServiceByName(packetRemove.serviceName()), true);

                            BridgeVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId()).getRank().equals(CloudRank.Admin)).forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The service <dark_gray>'<#37b4b4>" + packetRemove.serviceName() + "<dark_gray>' <white>is successfully shutting down.")));
                            BridgeVelocityStartup.getInstance().getProxyServer().unregisterServer(BridgeVelocityStartup.getInstance().getProxyServer().getServer(packetRemove.serviceName()).orElse(null).getServerInfo());
                        } else {
                            System.out.println("This service isn't connected with the proxy service.");
                        }
                    }

                    if (packet instanceof PacketPlayOutCloudPlayerConnect packetConnect) {
                        BridgeVelocityStartup.getInstance().getProxyServer().getPlayer(packetConnect.uniqueId()).ifPresent(player -> player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(packetConnect.minecraftService()).get()).connect());
                    }
                } else {
                    if (packet instanceof PacketPlayOutServiceAdd packetAdd) {
                        TeriumBridge.getInstance().getServiceManager().addService(new MinecraftService(TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(packetAdd.serviceGroup()), packetAdd.serviceId(), packetAdd.port()), true);
                    }

                    if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                        if (TeriumBridge.getInstance().getServiceManager().getServiceByName(packetRemove.serviceName()) != null) {
                            TeriumBridge.getInstance().getServiceManager().removeService(TeriumBridge.getInstance().getServiceManager().getServiceByName(packetRemove.serviceName()), true);
                        } else {
                            System.out.println("This service isn't connected with the proxy service.");
                        }
                    }
                }
            }
        });
    }
}
