package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SecureWorldNameUtil;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GCloneCommand extends ArgsCommand {

    public GCloneCommand() {
        super("gclone", 2, "Clone.use", "gworld.commands.clone");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        startClone(player, args[0], args[1]);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        startClone(console, args[0], args[1]);
        return true;
    }

    @Override
    protected CompletionNode createCompletions(CommandSender sender) {
        CompletionNode rootNode = new CompletionNode("gclone");

        for(String world : GWorld.getInstance().getDataHandler().getWorlds().keySet()) {
            rootNode.addChild(new CompletionNode(world));
        }

        return rootNode;
    }

    private void startClone(CommandSender sender, String worldName, String targetName) {
        if (!SecureWorldNameUtil.isSecuredWorldName(targetName)) {
            sender.sendMessage(SendMessageUtil.sendMessage("SecurityMessage"));
            return;
        }

        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresentOrElse(
                world -> {
                    sender.sendMessage(world.clone(targetName)
                            .map(api -> SendMessageUtil.sendMessage("Clone.success").replace("%world%", worldName).replace("%targetworld%", targetName))
                            .orElse(SendMessageUtil.sendMessage("Clone.failed").replace("%world%", targetName)));
                },
                () -> sender.sendMessage(SendMessageUtil.sendMessage("Clone.failed").replace("%world%", worldName))
        );
    }
}
