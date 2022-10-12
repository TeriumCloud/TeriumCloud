package cloud.terium.cloudsystem.webinterface;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.webinterface.html.Loader;
import cloud.terium.cloudsystem.webinterface.routes.LoginPostRoute;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import spark.Spark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class WebServer {

    @SneakyThrows
    public WebServer() {
        Spark.port(Terium.getTerium().getConfigManager().getInt("web_port"));
        Spark.get("/", (request, response) -> {
            response.redirect("/dashboard");
            return "Test";
        });
        Spark.post("/login", new LoginPostRoute());
    }

    public static String getHtmlString(String name) {
        InputStream inputStream = Loader.class.getResourceAsStream(name + ".html");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String result = "";
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result = result + "\n";
                result = result + line;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void stopWebServer() {
        Spark.stop();
    }
}