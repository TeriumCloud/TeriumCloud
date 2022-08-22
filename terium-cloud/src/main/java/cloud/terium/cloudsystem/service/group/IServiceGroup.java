package cloud.terium.cloudsystem.service.group;

import cloud.terium.cloudsystem.service.ServiceType;

public interface IServiceGroup {

    String name();

    String groupTitle();

    ServiceType serviceType();

    boolean maintenance();

    int port();

    int maximumPlayers();

    int memory();

    int minimalServices();

    int maximalServices();
}
