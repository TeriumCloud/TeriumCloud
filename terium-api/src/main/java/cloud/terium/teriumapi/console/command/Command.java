package cloud.terium.teriumapi.console.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class Command {

    private final String command;
    private final String help;
    private final String description;
    private final String[] aliases;
    private final List<String[]> arguments;

    public abstract void execute(String[] args);
}