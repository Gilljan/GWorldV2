package de.gilljan.gworld.commands;

import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.commands.tabcompletion.TabCompleter;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ArgsCommand implements CommandExecutor, org.bukkit.command.TabCompleter {
    private final String commandName;
    private final int expectedArgs;
    private final String usageMessageKey;
    private final String permission;

    protected ArgsCommand(String commandName, int expectedArgs, @NotNull String usageMessageKey, @Nullable String permission) {
        this.commandName = commandName;
        this.expectedArgs = expectedArgs;
        this.usageMessageKey = usageMessageKey;
        this.permission = permission;
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase(commandName)) {
            return false;
        }

        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(SendMessageUtil.sendMessage("NoPerm"));

            return false;
        }

        if (args.length < expectedArgs) {
            sender.sendMessage(SendMessageUtil.sendMessage(usageMessageKey));

            return false;
        }

        if (sender instanceof Player player) {
            return executeCommandForPlayer(player, args);
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            return executeConsoleCommand(consoleCommandSender, args);
        }

        return false;
    }

    @Override
    public @org.jetbrains.annotations.Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(this.permission != null && !sender.hasPermission(this.permission)) {
            return List.of();
        }

        return new TabCompleter(createCompletions(sender)).onTabComplete(sender, command, label, args);
    }

    public abstract boolean executeCommandForPlayer(Player player, String[] args);

    public abstract boolean executeConsoleCommand(ConsoleCommandSender console, String[] args);

    protected abstract CompletionNode createCompletions(CommandSender sender);
}
