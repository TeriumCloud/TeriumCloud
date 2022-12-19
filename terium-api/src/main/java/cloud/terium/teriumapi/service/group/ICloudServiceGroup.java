package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.cluster.ICluster;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.template.ITemplate;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public interface ICloudServiceGroup {

    /**
     * Get the name of the service group
     *
     * @return String This returns the name of the service group as String.
     */
    String getServiceGroupName();

    /**
     * Get the title of the service group
     *
     * @return String This returns the title of the service group as String.
     */
    String getGroupTitle();

    /**
     * Get the cluster of the service group
     *
     * @return String This returns the node name of the service group as String.
     */
    ICluster getServiceGroupCluster();

    /**
     * Get the templates of the service group
     *
     * @return List<ITemplate> This returns the template of the service group as ITemplate.
     */
    List<ITemplate> getTemplates();

    /**
     * Get the service type of the service group
     *
     * @return ServiceType This returns the service type of the service group as ServiceType.
     */
    ServiceType getServiceType();

    /**
     * Get the version of the service group
     *
     * @return String This returns the version as string.
     */
    String getVersion();

    /**
     * Get if the service group is in maintenance
     *
     * @return boolean This returns if the serivce group is in maintenance as boolean. (true if yes else false)
     */
    boolean isMaintenance();

    /**
     * Get if the service group is statc
     *
     * @return boolean This returns if the serivce group is static as boolean. (true if yes else false)
     */
    boolean isStatic();

    /**
     * Get if the service group has a port
     *
     * @return boolean This returns if the service group has a defined port as boolean. (true if yes else false)
     */
    boolean hasPort();

    /**
     * Get the port of the service group (only if the service group is a proxy group | if group is not proxy its returns -1)
     *
     * @return int This returns the port of the service group as string.
     */
    int getPort();

    /**
     * Get the maximal players who can join on the service group
     *
     * @return int This returns the maximal player who can join as int.
     */
    int getMaxPlayers();

    /**
     * Get the max memory of the service group
     *
     * @return int This returns the maximal memory of the service group as int.
     */
    int getMemory();

    /**
     * Get the minimal online service count of the service group
     *
     * @return int This returns the minimal online service count from the service group as int.
     */
    int getMinServices();

    /**
     * Get the maximal online service count of the service group
     *
     * @return int This returns the maximal online service count from the service group as int.
     */
    int getMaxServices();

    /**
     * Set the maximal players who can join on the service group
     *
     * @param players Int
     */
    void setMaxPlayer(int players);

    /**
     * Set the maintenance state of the service group
     *
     * @param maintenance Boolean
     */
    void setMaintenance(boolean maintenance);

    /**
     * Set the static value of the service group
     *
     * @param isStatic Boolean
     */
    void setStatic(boolean isStatic);

    /**
     * Set the max memory of the service group
     *
     * @param memory Int
     */
    void setMemory(int memory);

    /**
     * Set the minimal online service count of the service group
     *
     * @param services Int
     */
    void setMinServices(int services);

    /**
     * Set the maximal online service count of the service group
     * @param services Int
     */
    void setMaxServices(int services);

    /**
     * Update every change by api to cloud.
     */
    void update();

    /**
     * Get all informations of the service group in a fancy JSON format
     *
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
