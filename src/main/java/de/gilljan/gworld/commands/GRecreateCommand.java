package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GRecreateCommand extends ArgsCommand {

    public GRecreateCommand() {
        super("grecreate", 2, "Recreate.use", "gworld.commands.grecreate");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        String worldName = args[0];
        boolean saveOldWorld = Boolean.parseBoolean(args[1]);

        reCreateWorld(player, worldName, saveOldWorld);

        return false;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        String worldName = args[0];
        boolean saveOldWorld = Boolean.parseBoolean(args[1]);


        reCreateWorld(console, worldName, saveOldWorld);

        return false;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("grecreate");
        List<CompletionNode> booleans = List.of(new CompletionNode("true"), new CompletionNode("false"));

        for(World world : Bukkit.getWorlds()) {
            CompletionNode worldNode = new CompletionNode(world.getName());
            worldNode.addChildren(booleans);
            root.addChild(worldNode);
        }

        return root;
    }

    private void reCreateWorld(CommandSender sender, String worldName, boolean saveOldWorld) {
        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresentOrElse(
                world -> {
                    if (!world.isMapLoaded()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Recreate.failed").replace("%world%", worldName));
                        System.out.println("World " + worldName + " is not loaded, cannot recreate.");
                        return;
                    }

                    System.out.println("#1");

                    String messageKey = world.reCreate(saveOldWorld) ? "Recreate.success" : "Recreate.failed";
                    sender.sendMessage(SendMessageUtil.sendMessage(messageKey).replace("%world%", world.getWorldName()));
                },
                () -> sender.sendMessage(SendMessageUtil.sendMessage("Recreate.failed").replace("%world%", worldName))// System.out.println("No ManageableWorld found for world: " + worldName + "  " + Bukkit.getWorlds())
        );
    }
}
