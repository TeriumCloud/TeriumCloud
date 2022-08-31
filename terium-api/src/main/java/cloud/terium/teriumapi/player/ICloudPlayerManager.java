package cloud.terium.teriumapi.player;

import java.util.List;
import java.util.UUID;

public interface ICloudPlayerManager {

    ICloudPlayer getCloudPlayer(String username);

    ICloudPlayer getCloudPlayer(UUID uniqueId);

    List<ICloudPlayer> getRegisteredPlayers();

    List<ICloudPlayer> getOnlinePlayers();
}
