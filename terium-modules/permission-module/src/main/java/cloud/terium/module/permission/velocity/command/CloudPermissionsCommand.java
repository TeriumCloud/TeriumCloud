package cloud.terium.module.permission.velocity.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CloudPermissionsCommand {

    public BrigadierCommand build(String name) {
        LiteralCommandNode<CommandSource> commandNode = LiteralArgumentBuilder.<CommandSource>literal(name)
                .requires(commandSource -> commandSource.hasPermission("terium.command.permissions"))
                .executes(this::help)
                .build();

        return new BrigadierCommand(commandNode);
    }

    private int help(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("All <#06bdf8>/cperms <white>commands:"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>user <user>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>user <user> setgroup <group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group list"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group> create"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group> add permission <permission>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group> remove permission <permission>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>reload"));
        
        return 1;
    }
}
