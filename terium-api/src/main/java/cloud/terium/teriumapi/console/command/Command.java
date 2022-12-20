package cloud.terium.teriumapi.console.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jline.builtins.Completers;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class Command {

    private final String command;
    private final String description;
    private final String[] aliases;

    public abstract void execute(String[] args);

    public List<String> tabComplete(String[] args) {
        return null;
    }
}