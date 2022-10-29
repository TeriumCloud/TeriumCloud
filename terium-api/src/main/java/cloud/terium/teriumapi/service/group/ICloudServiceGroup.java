package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.template.ITemplate;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public interface ICloudServiceGroup {

    /**
     * Get the name of the service group
     * @return String This returns the name of the service group as String.
     */
    String getServiceGroupName();

    /**
     * Get the title of the service group
     * @return String This returns the title of the service group as String.
     */
    String getGroupTitle();

    /**
     * Get the node name of the service group
     * @return String This returns the node name of the service group as String.
     */
    String getServiceGroupNode();

    /**
     * Get the template of the service group
     * @return ITemplate This returns the template of the service group as ITemplate.
     */
    ITemplate getTemplate();

    /**
     * Get the service type of the service group
     * @return CloudServiceType This returns the service type of the service group as CloudServiceType.
     */
    CloudServiceType getServiceType();

    /**
     * Get the version of the service group
     * @return String This returns the version as string.
     */
    String getVersion();

    /**
     * Get if the service group is in maintenance
     * @return boolean This returns if the serivce group is in maintenance as boolean. (true if yes else false)
     */
    boolean isMaintenance();

    /**
     * Get if the service group has a port
     * @return boolean This returns if the service group has a defined port as boolean. (true if yes else false)
     */
    boolean hasPort();

    /**
     * Get the port of the service group (only if the service group is a proxy group | if group is not proxy its returns -1)
     * @return int This returns the port of the service group as string.
     */
    int getPort();

    /**
     * Get the maximal players who can join on the service group
     * @return int This returns the maximal player who can join as int.
     */
    int getMaximumPlayers();

    /**
     * Get the max memory of the service group
     * @return int This returns the maximal memory of the service group as int.
     */
    int getMemory();

    /**
     * Get the minimal online service count of the service group
     * @return int This returns the minimal online service count from the service group as int.
     */
    int getMinimalServices();

    /**
     * Get the maximal online service count of the service group
     * @return int This returns the maximal online service count from the service group as int.
     */
    int getMaximalServices();

    /**
     * Get all informations of the service group in a fancy JSON format
     * @return String This returns all informations from the service group in a JSON format as String.
     */
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
