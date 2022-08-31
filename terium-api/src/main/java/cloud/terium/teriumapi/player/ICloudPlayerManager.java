package cloud.terium.teriumapi.player;

import java.util.List;
import java.util.UUID;

public interface ICloudPlayerManager {

    ICloudPlayer getCloudPlayerByName(String username);

    ICloudPlayer getCloudPlayerByUniqueId(UUID uniqueId);

    List<ICloudPlayer> getRegisteredPlayers();

    List<ICloudPlayer> getOnlinePlayers();
}
