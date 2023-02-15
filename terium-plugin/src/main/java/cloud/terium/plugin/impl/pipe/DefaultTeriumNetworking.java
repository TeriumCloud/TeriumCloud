package cloud.terium.plugin.impl.pipe;

import cloud.terium.networking.client.TeriumClient;
import cloud.terium.networking.packet.group.PacketPlayOutGroupAdd;
import cloud.terium.networking.packet.node.PacketPlayOutNodeAdd;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerAdd;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateAdd;
import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.impl.node.Node;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.IDefaultTeriumNetworking;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import cloud.terium.teriumapi.service.impl.CloudService;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.impl.Template;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DefaultTeriumNetworking implements IDefaultTeriumNetworking {

    private final TeriumClient teriumClient;

    @SneakyThrows
    public DefaultTeriumNetworking() {
        teriumClient = new TeriumClient(System.getProperty("netty-address"), Integer.parseInt(System.getProperty("netty-port")));
        getChannel().pipeline().addLast(new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet) {
                System.out.println(packet.getClass().getSimpleName());
                // Nodes
                try {
                    if (packet instanceof PacketPlayOutNodeAdd newPacket)
                        TeriumPlugin.getInstance().getNodeProvider().getAllNodes().add(new Node(newPacket.name(), newPacket.key(), newPacket.address(), newPacket.memory(), newPacket.connected()));

                    // Templates
                    if (packet instanceof PacketPlayOutTemplateAdd newPacket)
                        TeriumPlugin.getInstance().getTemplateProvider().getAllTemplates().add(new Template(newPacket.name(), Path.of(newPacket.path())));

                    // Groups
                    if (packet instanceof PacketPlayOutGroupAdd newPacket) {
                        List<INode> fallbackNodes = new ArrayList<>();
                        List<ITemplate> templates = new ArrayList<>();
                        newPacket.fallbackNodes().forEach(s -> fallbackNodes.add(TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(s).orElseGet(null)));
                        newPacket.templates().forEach(s -> templates.add(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(s).orElseGet(null)));

                        switch (newPacket.serviceType()) {
                            case Lobby ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultLobbyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), fallbackNodes, templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Server ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultServerGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), fallbackNodes, templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                            case Proxy ->
                                    TeriumPlugin.getInstance().getServiceGroupProvider().getAllServiceGroups().add(new DefaultProxyGroup(newPacket.name(), newPacket.groupTitle(), TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(newPacket.node()).orElseGet(null), fallbackNodes, templates, newPacket.version(), newPacket.maintenance(), newPacket.isStatic(), newPacket.port(), newPacket.maximumPlayers(), newPacket.memory(), newPacket.minimalServices(), newPacket.maximalServices()));
                        }
                    }

                    // Services
                    if (packet instanceof PacketPlayOutCreateService newPacket)
                        TeriumPlugin.getInstance().getServiceProvider().getAllCloudServices().add(new CloudService(newPacket.serviceName(), newPacket.serviceId(), newPacket.port(), newPacket.parsedNode().orElseGet(null), newPacket.parsedServiceGroup().orElseGet(null), newPacket.parsedTemplates()));

                    // Players
                    if(packet instanceof PacketPlayOutCloudPlayerAdd newPacket) {
                        switch (newPacket.online()) {
                            case true -> TeriumPlugin.getInstance().getCloudPlayerProvider().getRegisteredPlayers().add(new ICloudPlayer() {
                                @Override
                                public String getUsername() {
                                    return newPacket.username();
                                }

                                @Override
                                public UUID getUniqueId() {
                                    return newPacket.uniquedId();
                                }

                                @Override
                                public String getAddress() {
                                    return newPacket.address().getHostName();
                                }

                                @Override
                                public Optional<ICloudService> getConnectedCloudService() {
                                    return newPacket.parsedCloudService();
                                }
                            });
                            case false -> TeriumPlugin.getInstance().getCloudPlayerProvider().getOnlinePlayers().add(new ICloudPlayer() {
                                @Override
                                public String getUsername() {
                                    return newPacket.username();
                                }

                                @Override
                                public UUID getUniqueId() {
                                    return newPacket.uniquedId();
                                }

                                @Override
                                public String getAddress() {
                                    return newPacket.address().getHostName();
                                }

                                @Override
                                public Optional<ICloudService> getConnectedCloudService() {
                                    return newPacket.parsedCloudService();
                                }
                            });
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
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
