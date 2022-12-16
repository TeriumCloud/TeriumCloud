package cloud.terium.networking;

import cloud.terium.networking.client.TeriumClient;
import lombok.SneakyThrows;

public class TeriumFramework {

    @SneakyThrows
    public static TeriumClient createClient(String host, int port) {
        return new TeriumClient(host, port);
    }
}
