package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GLoadCommand extends ArgsCommand {

    public GLoadCommand() {
        super("gload", 1, "Load.use", "gworld.commands.gload");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        loadWorld(player, args[0]);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        loadWorld(console, args[0]);
        return true;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("gload");

        for(String world : GWorld.getInstance().getDataHandler().getWorlds().keySet()) {
            root.addChild(new CompletionNode(world));
        }

        return root;
    }

    private void loadWorld(CommandSender sender, String worldName) {
        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresentOrElse(
                manageableWorld -> {
                    if(manageableWorld.isMapLoaded()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Load.alreadyLoaded").replace("%world%", worldName));
                        return;
                    }

                    sender.sendMessage(SendMessageUtil.sendMessage("Load.loading").replace("%world%", worldName));

                    if (manageableWorld.loadMap()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Load.success").replace("%world%", worldName));
                        return;
                    }

                    sender.sendMessage(SendMessageUtil.sendMessage("Load.failed").replace("%world%", worldName));
                },
                () -> sender.sendMessage(SendMessageUtil.sendMessage("Load.failed").replace("%world%", worldName))
        );
    }

}
