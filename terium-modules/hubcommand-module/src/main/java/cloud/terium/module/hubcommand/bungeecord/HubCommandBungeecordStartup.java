package cloud.terium.module.hubcommand.bungeecord;

import cloud.terium.module.hubcommand.bungeecord.command.HubCommand;
import cloud.terium.module.hubcommand.manager.ConfigManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class HubCommandBungeecordStartup extends Plugin {

    private static HubCommandBungeecordStartup instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand());
    }
}
