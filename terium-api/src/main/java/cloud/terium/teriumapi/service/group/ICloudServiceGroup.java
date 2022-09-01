package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.service.CloudServiceType;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public interface ICloudServiceGroup {

    /*
     * TODO: Write documentation
     */

    String getServiceGroupName();

    String getGroupTitle();

    String getServiceGroupNode();

    CloudServiceType getServiceType();

    boolean isMaintenance();

    boolean hasPort();

    int getPort();

    int getMaximumPlayers();

    int getMemory();

    int getMinimalServices();

    int getMaximalServices();

    @SneakyThrows
    default String getInformations() {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(new File("groups/" + getServiceType() + "/" + getServiceGroupName() + ".json").toPath()), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject().toString().replace("{", "{\n    ").replace(":", ": ").replace("}", "\n}").replace(",", ",\n    ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Error while getting information of " + getServiceGroupName() + ".";
    }
}
