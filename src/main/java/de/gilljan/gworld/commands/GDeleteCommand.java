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
        super("gdelete", 1, "Delete.use", "gworld.commands.gdelete");
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
            sender.sendMessage(SendMessageUtil.sendMessage("Delete.failed").replaceAll("%world%", worldName));
            return;
        }

        World mainWorld = Bukkit.getWorld(GWorld.getInstance().getConfig().getString("MainWorld"));
        for(Player player : mainWorld.getPlayers()) {
            player.teleport(mainWorld.getSpawnLocation());
            player.sendMessage(SendMessageUtil.sendMessage("Delete.teleport_players").replaceAll("%world%", worldName));
        }

        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresent(world -> {
            if(!world.deleteMap()) {
                sender.sendMessage(SendMessageUtil.sendMessage("Delete.failed").replaceAll("%world%", worldName));
                return;
            }

            sender.sendMessage(SendMessageUtil.sendMessage("Delete.success").replaceAll("%world%", worldName));
        });
    }
}
