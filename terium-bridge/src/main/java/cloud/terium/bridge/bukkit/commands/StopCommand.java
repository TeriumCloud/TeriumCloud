package cloud.terium.bridge.bukkit.commands;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.BridgeBukkitStartup;
import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.networking.packets.PacketPlayOutCloudPlayerConnect;
import cloud.terium.networking.packets.PacketPlayOutServiceForceShutdown;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (sender instanceof Player player) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "Trying to stop this service..."));
            }

            Bukkit.getOnlinePlayers().forEach(player -> player.kick(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#ef7b7b>The service you are connected with is shutting down.")));
            TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceForceShutdown(TeriumBridge.getInstance().getThisName()));
        }
        return false;
    }
}
