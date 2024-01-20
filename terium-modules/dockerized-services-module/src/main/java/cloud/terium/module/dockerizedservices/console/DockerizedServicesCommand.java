package cloud.terium.module.dockerizedservices.console;

import cloud.terium.module.dockerizedservices.TeriumDockerizedServices;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DockerizedServicesCommand extends Command {

    public DockerizedServicesCommand() {
        super("docker-services", "Modify and manage the dockerized-service-module", "dockerized-services");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 3) {
            if (args[0].equals("add")) {
                TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(args[1]).ifPresentOrElse(serviceGroup -> {
                    JsonObject groupJson = new JsonObject();
                    groupJson.addProperty("name", serviceGroup.getGroupName());
                    groupJson.addProperty("java-image", args[2]);
                    TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().getJson().getAsJsonObject().add(serviceGroup.getGroupName(), groupJson);
                    TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().save();
                    TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Successfully added group '§b" + serviceGroup.getGroupName() + "'§f to dockerized-services.", LogType.INFO);
                    TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().unbindServiceGroup(serviceGroup);
                    TeriumDockerizedServices.getInstance().getServiceFactory().bindServiceGroup(serviceGroup);
                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(serviceGroup.getGroupName()).forEach(ICloudService::shutdown);
                }, () -> TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("A service group with that name isn't registered.", LogType.ERROR));
            }

            return;
        }

        if (args.length == 2) {
            if (args[0].equals("remove")) {
                TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(args[1]).ifPresentOrElse(serviceGroup -> {
                    if (!TeriumDockerizedServices.getInstance().getServiceFactory().containsServiceGroup(serviceGroup)) {
                        TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("This service isn't registered for dockerized-services.", LogType.ERROR);
                        return;
                    }

                    TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().getJson().remove(serviceGroup.getGroupName());
                    TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().save();
                    TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Successfully removed group '§b" + serviceGroup.getGroupName() + "'§f from dockerized-services.", LogType.INFO);
                    TeriumDockerizedServices.getInstance().getServiceFactory().unbindServiceGroup(serviceGroup);
                    TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().bindServiceGroup(serviceGroup);
                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(serviceGroup.getGroupName()).forEach(ICloudService::shutdown);
                }, () -> TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("A service group with that name isn't registered.", LogType.ERROR));
            }

            return;
        }

        if (args.length == 1) {
            if (args[0].equals("list")) {
                TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("All dockerized service-groups:", LogType.INFO);
                TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("", LogType.INFO);
                TeriumDockerizedServices.getInstance().getConfigLoader().getIncludedGroupsLoader().getJson().keySet().forEach(s -> {
                    TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("Services from group '§b" + s + "§f':", LogType.INFO);
                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(s).stream().sorted(Comparator.comparing(ICloudService::getServiceName)).forEach(service ->
                            TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole(" §7● §fName: " + service.getServiceName() + " | State: " + service.getServiceState() + " | Players: " + service.getOnlinePlayers() + "/" + service.getMaxPlayers(), LogType.INFO));

                    TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("", LogType.INFO);
                });
            }

            return;
        }

        TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("docker-services add [group] [repo:tag] | Add a group to dockerized-services with image", LogType.INFO);
        TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("docker-services remove [group] | Add a group to dockerized-services", LogType.INFO);
        //TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("docker-services edit [group] [repo:tag] | Edit the image(repo:tag) of a group from dockerized-services", LogType.INFO);
        TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole("docker-services list | List all service groups of dockerized-services", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) return Arrays.asList("add", "remove", "list");

        if (args.length == 2 && args[0].equals("add"))
            return TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().stream().filter(serviceGroup -> !TeriumDockerizedServices.getInstance().getServiceFactory().containsServiceGroup(serviceGroup)).map(ICloudServiceGroup::getGroupName).toList();

        if (args.length == 2 && (args[0].equals("remove") || args[0].equals("edit")))
            return TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().stream().filter(serviceGroup -> TeriumDockerizedServices.getInstance().getServiceFactory().containsServiceGroup(serviceGroup)).map(ICloudServiceGroup::getGroupName).toList();

        if (args.length == 3 && (args[0].equals("add") || args[0].equals("edit")))
            return Arrays.asList("openjdk:17-alpine", "openjdk:20-ea-4-jdk", "openjdk:16-ea-34-jdk");

        return super.tabComplete(args);
    }
}