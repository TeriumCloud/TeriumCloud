package cloud.terium.cloudsystem.utils.logger;

import cloud.terium.cloudsystem.Terium;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
// [34m

    public static void log(String message, LogType logType) {
        Terium.getTerium().getConsoleManager().getLineReader().printAbove(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + logType.getPrefix() + message));
    }

    public static void log(String message) {
        System.out.println(message);
    }

    @SneakyThrows
    public static void licenseLog(String message, LogType logType) {
        System.out.println(("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "\u001B[0m] " + logType.getPrefix() + message));
    }
}