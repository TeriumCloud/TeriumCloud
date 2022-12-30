package cloud.terium.teriumapi.console;

import cloud.terium.teriumapi.console.command.Command;

import java.io.Serializable;

public interface IConsoleProvider extends Serializable {

    /**
     * Send a log message with log type to cloud instance
     *
     * @param message
     * @param logType
     */
    void sendConsole(String message, LogType logType);

    /**
     * Let the cloud execute the defined command
     *
     * @param command
     */
    void executeCommand(String command);
}