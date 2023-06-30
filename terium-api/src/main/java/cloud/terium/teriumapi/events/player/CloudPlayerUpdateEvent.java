package cloud.terium.teriumapi.events.player;

import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.Getter;

import java.net.InetSocketAddress;

@Getter
public class CloudPlayerUpdateEvent extends Event {

    private final ICloudPlayer cloudPlayer;
    private final String username;
    private final InetSocketAddress address;
    private final String value;
    private final String signature;
    private final ICloudService connectedService;

    public CloudPlayerUpdateEvent(ICloudPlayer cloudPlayer, String username, InetSocketAddress address, String value, String signature, ICloudService connectedService) {
        this.cloudPlayer = cloudPlayer;
        this.username = username;
        this.address = address;
        this.value = value;
        this.signature = signature;
        this.connectedService = connectedService;
    }
}