package cloud.terium.cloudsystem.service.group;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.service.ServiceType;
import com.google.gson.JsonParser;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Setter
public class DefaultServiceGroup implements IServiceGroup {

    private String name;
    private String groupTitle;
    private final ServiceType serviceType;
    private boolean maintenance;
    private int port;
    private int maximumPlayers;
    private int memory;
    private int minimalServices;
    private int maximalServices;

    public DefaultServiceGroup(String name, String groupTitle, ServiceType serviceType, boolean maintenance, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.serviceType = serviceType;
        this.port = -1;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        this.maintenance = maintenance;
        new File("templates//" + name).mkdirs();
    }

    public DefaultServiceGroup(String name, String groupTitle, ServiceType serviceType, boolean maintenance, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.serviceType = serviceType;
        this.port = port;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        this.maintenance = maintenance;
        new File("templates//" + name).mkdirs();
    }

    public void createGroup() {
        Terium.getTerium().getServiceGroupManager().createServiceGroup(this);
    }

    public boolean hasPort() {
        return port != -1;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String groupTitle() {
        return groupTitle;
    }

    @Override
    public ServiceType serviceType() {
        return serviceType;
    }

    @Override
    public boolean maintenance() {
        return maintenance;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public int maximumPlayers() {
        return maximumPlayers;
    }

    @Override
    public int memory() {
        return memory;
    }

    @Override
    public int minimalServices() {
        return minimalServices;
    }

    @Override
    public int maximalServices() {
        return maximalServices;
    }

    @SneakyThrows
    public String toString() {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(new File("groups/" + serviceType + "/" + name + ".json").toPath()), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject().toString().replace("{", "{\n    ").replace(":", ": ").replace("}", "\n}").replace(",", ",\n    ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Error while getting information of " + name + ".";
    }
}
