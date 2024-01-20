package cloud.terium.teriumapi.console;

import java.io.Serializable;

public interface IConsoleProvider extends Serializable {

    /**
     * Send a log message with log type to cloud instance
     *
     * @param message
     * @param logType
     */
    void sendConsole(String message, LogType logType);
}