package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
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

    private void loadWorld(CommandSender sender, String worldName) {
        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresentOrElse(
                manageableWorld -> {
                    if(manageableWorld.isMapLoaded()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Load.alreadyLoaded").replaceAll("%world%", worldName));
                        return;
                    }

                    sender.sendMessage(SendMessageUtil.sendMessage("Load.loading").replaceAll("%world%", worldName));

                    if (manageableWorld.loadMap()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("Load.success").replaceAll("%world%", worldName));
                        return;
                    }

                    sender.sendMessage(SendMessageUtil.sendMessage("Load.failed").replaceAll("%world%", worldName));
                },
                () -> sender.sendMessage(SendMessageUtil.sendMessage("Load.failed").replaceAll("%world%", worldName))
        );
    }

}
