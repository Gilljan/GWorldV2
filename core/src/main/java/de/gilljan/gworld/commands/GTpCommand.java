package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GTpCommand extends ArgsCommand {

    public GTpCommand() {
        super("gtp", 1, "Teleport.use", "gworld.commands.tp");
    }


    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        WorldData worldData = GWorld.getInstance().getDataHandler().getWorld(args[0]).orElse(null);

        if (worldData == null || !worldData.isLoaded()) {
            player.sendMessage(SendMessageUtil.sendMessage("Teleport.failed"));
            return false;
        }

        String worldName = worldData.getGeneralInformation().worldName();
        if (!player.hasPermission("gworld.commands.tp.*") && !player.hasPermission("gworld.commands.tp." + worldName)) {
            player.sendMessage(SendMessageUtil.sendMessage("Teleport.no_perm_world").replace("%world%", worldName));
            return false;
        }

        World world = Bukkit.getWorld(worldData.getGeneralInformation().worldName());

        if (args.length == 2) {
            if (!player.hasPermission("gworld.tp.other")) {
                player.sendMessage(SendMessageUtil.sendMessage("Teleport.no_perm_other"));
                return false;
            }

            if (args[1].equalsIgnoreCase("@all")) {
                List<Player> players = Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equalsIgnoreCase(player.getName())).collect(Collectors.toList());

                for(Player p : players) {
                    p.teleport(world.getSpawnLocation());
                    p.sendMessage(SendMessageUtil.sendMessage("Teleport.player_target_success").replace("%world%", world.getName()).replace("%player%", player.getName()));
                }

                player.sendMessage(SendMessageUtil.sendMessage("Teleport.sender_target_all_success").replace("%world%", world.getName()).replace("%count%", String.valueOf(players.size())));
                return true;
            }

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

        if (world == null) {
            player.sendMessage(SendMessageUtil.sendMessage("Teleport.failed"));
            return false;
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
    protected CompletionNode createCompletions(CommandSender sender) {
        CompletionNode root = new CompletionNode("gtp");

        GWorld.getInstance().getDataHandler().getWorlds().values().stream()
                .filter(WorldData::isLoaded)
                .filter(world -> sender.hasPermission("gworld.commands.tp.*") || sender.hasPermission("gworld.commands.tp." + world.getGeneralInformation().worldName()))
                .forEach(world -> {
                    CompletionNode worldNode = new CompletionNode(world.getGeneralInformation().worldName());
                    if (sender.hasPermission("gworld.tp.other")) {
                        List<CompletionNode> players = Bukkit.getOnlinePlayers().stream().map(player -> new CompletionNode(player.getName())).toList();
                        CompletionNode allNode = new CompletionNode("@all");
                        worldNode.addChildren(players);
                        worldNode.addChild(allNode);
                    }
                    root.addChild(worldNode);
                });

        return root;
    }
}
