package cloud.terium.cloudsystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Command {

    private final String name;

    public abstract void execute(String[] args);
}