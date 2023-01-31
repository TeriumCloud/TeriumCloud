package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.template.ITemplate;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record PacketPlayOutCreateServerGroup(String name, String groupTitle, INode node, List<INode> fallbackNodes,
                                             List<ITemplate> templates,
                                             String version, boolean maintenance, boolean isStatic, int maximumPlayers,
                                             int memory, int minimalServices, int maximalServices) implements Packet {

    @Override
    public List<ITemplate> templates() {
        List<ITemplate> templateList = new ArrayList<>();
        templates.forEach(template -> {
            templateList.add(new ITemplate() {
                @Override
                public String getName() {
                    return template.getName();
                }

                @Override
                public Path getPath() {
                    return template.getPath();
                }
            });
        });
        return templateList;
    }

    @Override
    public INode node() {
        return new INode() {
            @Override
            public String getName() {
                return node.getName();
            }

            @Override
            public String getKey() {
                return node.getKey();
            }

            @Override
            public InetSocketAddress getAddress() {
                return node.getAddress();
            }

            @Override
            public boolean isConnected() {
                return node.isConnected();
            }

            @Override
            public long getUsedMemory() {
                return node.getUsedMemory();
            }

            @Override
            public void setUsedMemory(long usedMemory) {
                node.setUsedMemory(usedMemory);
            }

            @Override
            public long getMaxMemory() {
                return node.getMaxMemory();
            }

            @Override
            public void setMaxMemory(long maxMemory) {
                node.setMaxMemory(maxMemory);
            }

            @Override
            public void update() {
                node.update();
            }

            @Override
            public void disconnect() {
                node.disconnect();
            }

            @Override
            public void stop() {
                node.stop();
            }
        };
    }

    @Override
    public List<INode> fallbackNodes() {
        List<INode> nodeList = new ArrayList<>();
        fallbackNodes.forEach(node -> {
            nodeList.add(new INode() {
                @Override
                public String getName() {
                    return node.getName();
                }

                @Override
                public String getKey() {
                    return node.getKey();
                }

                @Override
                public InetSocketAddress getAddress() {
                    return node.getAddress();
                }

                @Override
                public boolean isConnected() {
                    return node.isConnected();
                }

                @Override
                public long getUsedMemory() {
                    return node.getUsedMemory();
                }

                @Override
                public void setUsedMemory(long usedMemory) {
                    node.setUsedMemory(usedMemory);
                }

                @Override
                public long getMaxMemory() {
                    return node.getMaxMemory();
                }

                @Override
                public void setMaxMemory(long maxMemory) {
                    node.setMaxMemory(maxMemory);
                }

                @Override
                public void update() {
                    node.update();
                }

                @Override
                public void disconnect() {
                    node.disconnect();
                }

                @Override
                public void stop() {
                    node.stop();
                }
            });
        });
        return nodeList;
    }
}