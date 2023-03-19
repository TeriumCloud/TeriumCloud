package cloud.terium.teriumapi.service;

import java.io.Serializable;

public enum ServiceState implements Serializable {
    PREPARING("#d1a879"),
    STARTING(""),
    ONLINE("#91d177"),
    INVISIBLE("#5c5c5c");

    private final String hex;

    ServiceState(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }
}