package cloud.terium.module.dockerizedservices.console;

import cloud.terium.teriumapi.console.command.Command;

public class DockerizedServicesCommand extends Command {

    public DockerizedServicesCommand() {
        super("docker-services", "Modify and manage the dockerized-service-module", "dockerized-services");
    }

    @Override
    public void execute(String[] strings) {

    }
}
