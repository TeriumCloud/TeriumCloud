package cloud.terium.cloudsystem.config;

import com.google.gson.JsonObject;

public record CloudConfig(String name, String ip, int port, int memory, String serviceAddress, String promt,
                          boolean debugMode, JsonObject nodes) {
}