package cloud.terium.cloudsystem.config;

import com.google.gson.JsonArray;

public record CloudConfig(String name, String ip, int port, int memory, String serviceAddress, String promt,
                          JsonArray nodes) {
}