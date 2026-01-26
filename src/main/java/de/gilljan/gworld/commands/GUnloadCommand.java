package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GUnloadCommand extends ArgsCommand {

    public GUnloadCommand() {
        super("gunload", 0, "Unload.use", "gworld.commands.gunload");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        if(args.length == 0) {
            World currentWorld = player.getWorld();
            unloadWorld(player, currentWorld.getName());
            return true;
        }

        unloadWorld(player, args[0]);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        unloadWorld(console, args[0]);
        return true;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("gunload");

        for(World world : Bukkit.getWorlds()) {
            root.addChild(new CompletionNode(world.getName()));
        }

        return root;
    }

    private void unloadWorld(CommandSender sender, String worldName) {
        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresentOrElse(
                manageableWorld -> {
                    if(!manageableWorld.isMapLoaded()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Unload.alreadyUnloaded").replace("%world%", worldName));
                        return;
                    }

                    sender.sendMessage(SendMessageUtil.sendMessage("Unload.unloading").replace("%world%", worldName));

                    if (manageableWorld.unloadMap()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Unload.success").replace("%world%", worldName));
                        return;
                    }

                    sender.sendMessage(SendMessageUtil.sendMessage("Unload.failed").replace("%world%", worldName));
                },
                () -> sender.sendMessage(SendMessageUtil.sendMessage("Unload.failed").replace("%world%", worldName))
        );

    }

}
