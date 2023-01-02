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

    /**
     * Constructor to create a command.
     *
     * @param command String
     * @param description String
     * @param aliases String[]
     */
    public Command(String command, String description, String... aliases) {
        this.command = command;
        this.description = description;
        this.aliases = aliases;
    }

    /**
     * What the command should execute.
     *
     * @param args
     */
    public abstract void execute(String[] args);

    /**
     * Overwrite that methode to write your own tab completion for the command.
     *
     * @param args
     * @return List<String>
     */
    public List<String> tabComplete(String[] args) {
        return List.of();
    }
}