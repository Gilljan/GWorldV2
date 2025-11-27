package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.data.world.WorldData;
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
        WorldData worldData = GWorld.getInstance().getDataHandler().getWorld(args[0]).orElse(null);

        if(worldData == null || !worldData.isLoaded()) {
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

            player.sendMessage(SendMessageUtil.sendMessage("Teleport.sender_target_success").replace("%world%", world.getName()).replace("%player%", target.getDisplayName()));
            target.sendMessage(SendMessageUtil.sendMessage("Teleport.player_target_success").replace("%world%", world.getName()).replace("%player%", player.getName()));

            return true;
        }

        player.teleport(world.getSpawnLocation());

        player.sendMessage(SendMessageUtil.sendMessage("Teleport.success").replace("%world%", world.getName()));

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

        GWorld.getInstance().getDataHandler().getWorlds().values().stream().filter(WorldData::isLoaded).forEach(world -> {
            CompletionNode worldNode = new CompletionNode(world.getGeneralInformation().worldName());
            worldNode.addChildren(players);
            root.addChild(worldNode);
        });

        return root;
    }
}
