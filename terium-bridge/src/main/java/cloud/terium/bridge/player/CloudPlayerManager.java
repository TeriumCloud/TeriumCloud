package cloud.terium.bridge.player;

import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerManager;
import cloud.terium.teriumapi.service.ICloudServiceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CloudPlayerManager implements ICloudPlayerManager {

    /*
     * TODO: Recode that class
     */
    private final Map<UUID, CloudPlayer> playerCache;

    public CloudPlayerManager() {
        this.playerCache = new HashMap<>();
    }

    public CloudPlayer getCloudPlayer(String playerName, UUID uniqueId) {
        if (playerCache.containsKey(uniqueId)) {
            return playerCache.get(uniqueId);
        }

        //ResultSet resultSet = TeriumBridge.getInstance().getMySQLManager().getDatabaseResult("SELECT * FROM playerData WHERE player_uuid='" + uniqueId + "'");

        // try {
            /*if (resultSet.next()) {
                CloudPlayer cloudPlayer = new CloudPlayer();

                cloudPlayer.setUsername(playerName);
                cloudPlayer.setUniqueId(uniqueId);
                cloudPlayer.setRank(CloudRank.valueOf(resultSet.getString("cloud_rank")));
                cloudPlayer.setConnectedService(TeriumBridge.getInstance().getServiceManager().getServiceByName(resultSet.getString("connected_server")));
                cloudPlayer.setTitle(resultSet.getString("title"));

                playerCache.put(uniqueId, cloudPlayer);
                return cloudPlayer;
            }*/

        CloudPlayer cloudPlayer = new CloudPlayer();
        cloudPlayer.setUsername(playerName);
        cloudPlayer.setUniqueId(uniqueId);
        if (playerName.equals("Jxnnik")) cloudPlayer.setRank(CloudRank.Admin); else cloudPlayer.setRank(CloudRank.VIP);
        cloudPlayer.setTitle(null);
        cloudPlayer.setConnectedService(null);
        // TeriumBridge.getInstance().getMySQLManager().updateDatabase("INSERT INTO playerData (player_uuid, player_name, cloud_rank, connected_server) VALUES ('" + uniqueId + "', '" + playerName + "', 'Default', 'null')");

        playerCache.put(uniqueId, cloudPlayer);
        return cloudPlayer;
        /*} catch (SQLException exception) {
            exception.printStackTrace();
        }*/

        // return null;
    }

    public void updateCloudPlayer(CloudPlayer cloudPlayer) {
        // TeriumBridge.getInstance().getMySQLManager().updateDatabase("UPDATE playerData SET player_name='" + cloudPlayer.getUsername() + "', cloud_rank='" + cloudPlayer.getRank() + "', connected_server='" + cloudPlayer.getConnectedService() + "' WHERE player_uuid='" + cloudPlayer.getUniqueId() + "'");
    }

    public void updateCloudPlayer(CloudPlayer cloudPlayer, boolean removeFromCache) {
        // TeriumBridge.getInstance().getMySQLManager().updateDatabase("UPDATE playerData SET player_name='" + cloudPlayer.getUsername() + "', cloud_rank='" + cloudPlayer.getRank() + "', connected_server='" + cloudPlayer.getConnectedService() + "' WHERE player_uuid='" + cloudPlayer.getUniqueId() + "'");
        playerCache.remove(cloudPlayer.getUniqueId());
    }

    @Override
    public ICloudPlayer getCloudPlayer(String s) {
        return null;
    }

    @Override
    public ICloudPlayer getCloudPlayer(UUID uuid) {
        return null;
    }

    @Override
    public List<ICloudPlayer> getRegisteredPlayers() {
        return null;
    }

    @Override
    public List<ICloudPlayer> getOnlinePlayers() {
        return null;
    }
}