package cloud.terium.teriumapi.console;

public interface ICloudConsoleProvider {

    /**
     * Send a log message with log type to cloud instance
     * @param message
     * @param logType
     */
    void sendConsole(String message, LogType logType);

    /**
     * Let the cloud execute the defined command
     * @param command
     */
    void executeCommand(String command);
}