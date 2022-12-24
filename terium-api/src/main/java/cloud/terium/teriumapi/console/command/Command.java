package cloud.terium.teriumapi.console.command;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class Command implements Serializable {

    private final String command;
    private final String description;
    private final String[] aliases;

    public Command(String command, String description, String... aliases) {
        this.command = command;
        this.description = description;
        this.aliases = aliases;
    }

    public abstract void execute(String[] args);

    public List<String> tabComplete(String[] args) {
        return List.of();
    }
}