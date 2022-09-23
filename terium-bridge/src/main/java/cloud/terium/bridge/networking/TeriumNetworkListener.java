package cloud.terium.bridge.networking;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.BridgeBukkitStartup;
import cloud.terium.bridge.impl.networking.DefaultTeriumNetworking;
import cloud.terium.bridge.player.CloudRank;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.networking.packet.Packet;
import cloud.terium.networking.packets.*;
import cloud.terium.teriumapi.service.CloudServiceState;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.impl.CloudService;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;

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
                if (packet instanceof PacketPlayOutServiceChangeState packetOnline) {
                    TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetOnline.minecraftService()).setServiceState(packetOnline.serviceState());
                }

                /*
                 * TODO: Code #reloadGroups(boolean) in Cloud & API Interface
                 */
                if (packet instanceof PacketPlayOutGroupsReload) {
                    // TeriumBridge.getInstance().getServiceGroupManager().reloadGroups(true);
                }

                /*
                 * TODO: Code #reloadGroup(File file) in Cloud & API Interface
                 */
                if (packet instanceof PacketPlayOutGroupReload groupReload) {
                    // TeriumBridge.getInstance().getServiceGroupManager().reloadGroup(new File("../../groups/" + TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(groupReload.serviceGroup()).serviceType() + "/" + groupReload.serviceGroup() + ".json"));
                }

                if (packet instanceof PacketPlayOutServiceMemoryUpdatePacket packetPlayOutServiceMemoryUpdatePacket) {
                    TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetPlayOutServiceMemoryUpdatePacket.minecraftService()).setUsedMemory(packetPlayOutServiceMemoryUpdatePacket.memory());
                }

                if(packet instanceof PacketPlayOutServiceOnlinePlayersUpdatePacket packetPlayOutServiceOnlinePlayersUpdatePacket) {
                    TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetPlayOutServiceOnlinePlayersUpdatePacket.minecraftService()).setOnlinePlayers(packetPlayOutServiceOnlinePlayersUpdatePacket.players());
                }

                /*if (packet instanceof PacketPlayOutServiceForceShutdown packetPlayOutServiceShutdown) {
                    if (TeriumBridge.getInstance().getThisService().getServiceType().equals(CloudServiceType.Proxy)) {
                        BridgeVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId()).getRank().equals(CloudRank.Admin)).forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The service <dark_gray>'<#37b4b4>" + packetPlayOutServiceShutdown.serviceName() + "<dark_gray>' <white>is shutting down...")));
                    }

                    if (TeriumBridge.getInstance().getThisName().equals(packetPlayOutServiceShutdown.serviceName())) {
                        if (TeriumBridge.getInstance().getThisService().getServiceType().equals(CloudServiceType.Proxy))
                            BridgeVelocityStartup.getInstance().getProxyServer().shutdown();
                        else BridgeBukkitStartup.getInstance().getServer().shutdown();
                    }
                }*/

                if (TeriumBridge.getInstance().getThisService().getServiceType().equals(CloudServiceType.Proxy)) {
                    if (packet instanceof PacketPlayOutServiceAdd packetAdd) {
                        TeriumBridge.getInstance().getServiceManager().addService(new CloudService(packetAdd.serviceName(), packetAdd.serviceId(), packetAdd.port(), TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(packetAdd.serviceGroup()), false));
                        if (BridgeVelocityStartup.getInstance().getProxyServer().getServer(packetAdd.serviceName()).isPresent()) {
                            return;
                        }

                        BridgeVelocityStartup.getInstance().getProxyServer().registerServer(new ServerInfo(packetAdd.serviceName(), new InetSocketAddress("127.0.0.1", packetAdd.port())));
                        BridgeVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId()).getRank().equals(CloudRank.Admin)).forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The service <dark_gray>'<#37b4b4>" + packetAdd.serviceName() + "<dark_gray>' <white>is starting...")));
                    }

                    if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                        if (TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetRemove.serviceName()) != null) {
                            TeriumBridge.getInstance().getServiceManager().removeService(TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetRemove.serviceName()));

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
                        TeriumBridge.getInstance().getServiceManager().addService(new CloudService(packetAdd.serviceName(), packetAdd.serviceId(), packetAdd.port(), TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(packetAdd.serviceGroup()), false));
                    }

                    if (packet instanceof PacketPlayOutServiceRemove packetRemove) {
                        if (TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetRemove.serviceName()) != null) {
                            TeriumBridge.getInstance().getServiceManager().removeService(TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(packetRemove.serviceName()));
                        } else {
                            System.out.println("This service isn't connected with the proxy service.");
                        }
                    }
                }
            }
        });
    }
}
