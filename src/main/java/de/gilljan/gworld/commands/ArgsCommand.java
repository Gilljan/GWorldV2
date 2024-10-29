package de.gilljan.gworld.commands;

import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class ArgsCommand implements CommandExecutor {
    private final String commandName;
    private final int expectedArgs;
    private final String usageMessageKey;
    private final String permission;

    public ArgsCommand(String commandName, int expectedArgs, @NotNull String usageMessageKey, @Nullable String permission) {
        this.commandName = commandName;
        this.expectedArgs = expectedArgs;
        this.usageMessageKey = usageMessageKey;
        this.permission = permission;
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(commandName)) {
            return false;
        }

        if (args.length < expectedArgs) {
            sender.sendMessage(SendMessageUtil.sendMessage(usageMessageKey));

            return false;
        }

        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(SendMessageUtil.sendMessage("NoPerm"));

            return false;
        }

        if (sender instanceof Player player) {
            return executeCommandForPlayer(player, args);
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            return executeConsoleCommand(consoleCommandSender, args);
        }

        return false;
    }

    public abstract boolean executeCommandForPlayer(Player player, String[] args);

    public abstract boolean executeConsoleCommand(ConsoleCommandSender console, String[] args);
}
