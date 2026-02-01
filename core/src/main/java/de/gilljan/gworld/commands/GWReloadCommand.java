package de.gilljan.gworld.commands;

import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Command is not in use anymore
 */
@Deprecated(since = "1.3.3", forRemoval = true)
public class GWReloadCommand extends ArgsCommand {
    public GWReloadCommand() {
        super("gwreload", 0, "Reload.use", "gworld.commands.reload");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        player.sendMessage(SendMessageUtil.sendMessage("Reload.info"));
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        console.sendMessage(SendMessageUtil.sendMessage("Reload.info"));
        return true;
    }

    @Override
    protected CompletionNode createCompletions(CommandSender sender) {
        return new CompletionNode("gwreload");
    }
}
