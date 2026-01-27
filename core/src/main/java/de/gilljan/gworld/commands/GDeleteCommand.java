package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GDeleteCommand extends ArgsCommand {

    public GDeleteCommand() {
        super("gdelete", 1, "Delete.use", "gworld.commands.delete");
    }


    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        deleteWorld(player, args[0]);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        deleteWorld(console, args[0]);
        return true;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("gdelete");

        for(World world : Bukkit.getWorlds()) {
            root.addChild(new CompletionNode(world.getName()));
        }

        return root;
    }

    private void deleteWorld(CommandSender sender, String worldName) {
        if(!GWorld.getInstance().getDataHandler().containsWorld(worldName)) {
            sender.sendMessage(SendMessageUtil.sendMessage("Delete.failed").replace("%world%", worldName));
            return;
        }

        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresent(world -> {
            if(!world.deleteMap()) {
                sender.sendMessage(SendMessageUtil.sendMessage("Delete.failed").replace("%world%", worldName));
                return;
            }

            sender.sendMessage(SendMessageUtil.sendMessage("Delete.success").replace("%world%", worldName));
        });
    }
}
