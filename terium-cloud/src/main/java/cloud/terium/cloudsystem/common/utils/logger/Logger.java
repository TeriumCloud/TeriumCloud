package cloud.terium.cloudsystem.common.utils.logger;

import cloud.terium.teriumapi.console.LogType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static void log(String message) {
        System.out.println(LoggerColors.replaceColorCodes("§f[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "§f] " + message));
    }

    public static void log(String message, LogType logType) {
        System.out.println(LoggerColors.replaceColorCodes("§f[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "§f] " + logType.getPrefix() + "§f" + message));
    }
}