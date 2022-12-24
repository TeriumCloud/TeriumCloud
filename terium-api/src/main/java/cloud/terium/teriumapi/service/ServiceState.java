package cloud.terium.teriumapi.service;

import java.io.Serializable;

public enum ServiceState implements Serializable {
    PREPARING,
    STARTING,
    ONLINE,
    INVISIBLE;
}