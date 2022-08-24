package cloud.terium.cloudsystem.utils.logger;

import lombok.Getter;

@Getter
public enum LogType {
    NOTHING(""),
    DOWNLOAD("\u001B[35mDOWNLOAD\u001B[0m: "),
    SETUP("\u001B[33mSETUP\u001B[0m: "),
    INFO("\u001B[36mINFO\u001B[0m: "),
    ERROR("\u001B[31mERROR\u001B[0m: ");

    private final String prefix;

    LogType(String prefix) {
        this.prefix = prefix;
    }
}
