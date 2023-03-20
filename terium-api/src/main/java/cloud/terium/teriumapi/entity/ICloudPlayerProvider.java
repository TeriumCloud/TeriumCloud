package cloud.terium.teriumapi.entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICloudPlayerProvider {

    /**
     * Get a cloud player by name(String)
     *
     * @param username The username of the cloud player.
     * @return ICloudPlayer This returns a cloud player by username
     */
    Optional<ICloudPlayer> getCloudPlayer(String username);

    /**
     * Get a cloud player by uuid(UUID)
     *
     * @param uniqueId The UUID of the cloud player.
     * @return ICloudPlayer This returns a cloud player by UUID
     */
    Optional<ICloudPlayer> getCloudPlayer(UUID uniqueId);

    /**
     * Get all online cloud players
     *
     * @return List<ICloudPlayer> This returns a list of all online cloud players
     */
    List<ICloudPlayer> getOnlinePlayers();
}
