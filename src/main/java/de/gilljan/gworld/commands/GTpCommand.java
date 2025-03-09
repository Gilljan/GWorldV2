package de.gilljan.gworld.commands;

import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GTpCommand extends ArgsCommand {

    public GTpCommand() {
        super("gtp", 1, "Teleport.use", "gworld.commands.gtp");
    }


    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        World world = Bukkit.getWorld(args[0]);

        if(world == null) {
            player.sendMessage(SendMessageUtil.sendMessage("Teleport.failed"));
            return false;
        }

        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null || !target.isOnline()) {
                player.sendMessage(SendMessageUtil.sendMessage("Teleport.failed"));
                return false;
            }

            target.teleport(world.getSpawnLocation());

            player.sendMessage(SendMessageUtil.sendMessage("Teleport.sender_target_success").replaceAll("%world%", world.getName()).replaceAll("%player%", target.getDisplayName()));
            target.sendMessage(SendMessageUtil.sendMessage("Teleport.player_target_success").replaceAll("%world%", world.getName()).replaceAll("%player%", player.getName()));

            return true;
        }

        player.teleport(world.getSpawnLocation());

        player.sendMessage(SendMessageUtil.sendMessage("Teleport.success").replaceAll("%world%", world.getName()));

        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        console.sendMessage(SendMessageUtil.sendMessage("Teleport.failed_console"));
        return false;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("gtp");
        List<CompletionNode> players = Bukkit.getOnlinePlayers().stream().map(player -> new CompletionNode(player.getName())).toList();

        for(World world : Bukkit.getWorlds()) {
            CompletionNode worldNode = new CompletionNode(world.getName());
            worldNode.addChildren(players);
            root.addChild(worldNode);
        }

        return root;
    }
}
