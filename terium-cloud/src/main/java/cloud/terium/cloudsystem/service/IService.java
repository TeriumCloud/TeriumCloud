package cloud.terium.cloudsystem.service;

import cloud.terium.cloudsystem.service.group.DefaultServiceGroup;

public interface IService {

    String serviceName();

    boolean online();

    int serviceId();

    int port();

    int maxPlayers();

    int onlinePlayers();

    int usedMemory();

    int maxMemory();

    DefaultServiceGroup defaultServiceGroup();

    ServiceType serviceType();
}
