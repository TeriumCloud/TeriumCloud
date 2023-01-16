package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.List;

public record PacketPlayOutRegisteredServices(List<ICloudService> cloudServices) {
}
