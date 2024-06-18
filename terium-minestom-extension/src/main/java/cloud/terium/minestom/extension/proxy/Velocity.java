package cloud.terium.minestom.extension.proxy;

import cloud.terium.minestom.extension.proxy.util.Proxy;

public class Velocity implements Proxy {

    private final String forwardingSecret;

    public Velocity(String forwardingSecret) {
        this.forwardingSecret = forwardingSecret;
    }

    public String getForwardingSecret() {
        return forwardingSecret;
    }
}