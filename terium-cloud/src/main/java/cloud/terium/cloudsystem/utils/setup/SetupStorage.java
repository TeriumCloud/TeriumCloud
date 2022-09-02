package cloud.terium.cloudsystem.utils.setup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetupStorage {

    private SetupType setupType;
    private boolean eula;
    private int webPort;
    private int proxyPort;
    private String proxyVersion;
    private String spigotVersion;
}
