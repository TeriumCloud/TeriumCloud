package cloud.terium.cloudsystem.webinterface;

import cloud.terium.cloudsystem.Terium;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

public class WebServer {

    private final HttpServer httpServer;

    @SneakyThrows
    public WebServer() {
        this.httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", Terium.getTerium().getConfigManager().getInt("web_port")), 0);
    }

    private void startWebServer() {
        this.httpServer.start();
    }

    public void stopWebServer() {
        this.httpServer.stop(0);
    }
}