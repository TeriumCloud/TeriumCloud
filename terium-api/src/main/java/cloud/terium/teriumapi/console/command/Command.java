package cloud.terium.teriumapi.console.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Command {

    private final String command;
    public final String[] aliases;
    private final String[] arguments;

    public abstract void execute(String[] args);
}