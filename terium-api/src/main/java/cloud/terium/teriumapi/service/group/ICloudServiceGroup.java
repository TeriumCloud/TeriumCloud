package cloud.terium.teriumapi.service.group;

import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.template.ITemplate;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public interface ICloudServiceGroup extends Serializable {

    /**
     * Get the name of the service group
     *
     * @return String This returns the name of the service group as String.
     */
    String getGroupName();

    /**
     * Get the title of the service group
     *
     * @return String This returns the title of the service group as String.
     */
    String getGroupTitle();

    /**
     * Get the node of the service group
     *
     * @return String This returns the node name of the service group as String.
     */
    INode getGroupNode();

    /**
     * Set the node of the service group
     *
     * @param node INode
     */
    void setGroupNode(INode node);

    /**
     * Get the templates of the service group
     *
     * @return List<ITemplate> This returns the templates of the service group as List<ITemplate>.
     */
    List<ITemplate> getTemplates();

    /**
     * Add a template to the service group
     *
     * @param template ITemplate
     */
    default void addTemplate(ITemplate template) {
        getTemplates().add(template);
    }

    /**
     * Remove a template to the service group
     *
     * @param template ITemplate
     */
    default void removeTemplate(ITemplate template) {
        getTemplates().remove(template);
    }

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
     * Set the version of the service group
     *
     * @param version String
     */
    void setVersion(String version);

    /**
     * Get if the service group is in maintenance
     *
     * @return boolean This returns if the serivce group is in maintenance as boolean. (true if yes else false)
     */
    boolean isMaintenance();

    /**
     * Set the maintenance state of the service group
     *
     * @param maintenance Boolean
     */
    void setMaintenance(boolean maintenance);

    /**
     * Get if the service group is statc
     *
     * @return boolean This returns if the serivce group is static as boolean. (true if yes else false)
     */
    boolean isStatic();

    /**
     * Set the static value of the service group
     *
     * @param isStatic Boolean
     */
    void setStatic(boolean isStatic);

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
     * Set the max memory of the service group
     *
     * @param memory Int
     */
    void setMemory(int memory);

    /**
     * Get the minimal online service count of the service group
     *
     * @return int This returns the minimal online service count from the service group as int.
     */
    int getMinServices();

    /**
     * Set the minimal online service count of the service group
     *
     * @param services Int
     */
    void setMinServices(int services);

    /**
     * Get the maximal online service count of the service group
     *
     * @return int This returns the maximal online service count from the service group as int.
     */
    int getMaxServices();

    /**
     * Set the maximal online service count of the service group
     *
     * @param services Int
     */
    void setMaxServices(int services);

    /**
     * Set the maximal players who can join on the service group
     *
     * @param players Int
     */
    void setMaxPlayer(int players);

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
        return "{\n" +
                "   group_name: " + getGroupName() + "\n" +
                "   group_title: " + getGroupTitle() + "\n" +
                "   node: " + getGroupNode().getName() + "\n" +
                "   templates: " + getTemplates().stream().map(ITemplate::getName).toList() + "\n" +
                "   version: " + getVersion() + "\n" +
                "   servicetype: " + getServiceType() + "\n" +
                "   maintenance: " + isMaintenance() + "\n" +
                "   static: " + isStatic() + "\n" +
                "   maximum_players: " + getMaxPlayers() + "\n" +
                "   memory: " + getMemory() + "\n" +
                "   minimal_services: " + getMinServices() + "\n" +
                "   maximal_services: " + getMaxServices() + "\n" +
                "}";
    }

    /**
     * Get all informations of the service group from the json file in a fancy JSON format
     *
     * @return String This returns all informations from the service group from the json file in a JSON format as String.
     */
    @SneakyThrows
    default String getInformationsFromJson() {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(new File("groups/" + getGroupName() + ".json").toPath()), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject().toString().replace("{", "{\n    ").replace(":", ": ").replace("}", "\n}").replace(",", ",\n    ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Error while getting information of " + getGroupName() + ".";
    }
}