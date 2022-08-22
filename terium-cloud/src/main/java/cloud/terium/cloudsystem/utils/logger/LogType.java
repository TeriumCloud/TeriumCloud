package cloud.terium.cloudsystem.utils.logger;

import lombok.Getter;

@Getter
public enum LogType {
    NOTHING(""),
    INFO("\u001B[36mINFO\u001B[0m: "),
    ERROR("\u001B[31mERROR\u001B[0m: ");

    private final String prefix;

    LogType(String prefix) {
        this.prefix = prefix;
    }
}
