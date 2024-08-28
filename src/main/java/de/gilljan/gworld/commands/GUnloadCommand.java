package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GUnloadCommand extends ArgsCommand {

    public GUnloadCommand() {
        super("gunload", 1, "Unload.use", "gworld.commands.gunload");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        unloadWorld(player, args[0]);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        unloadWorld(console, args[0]);
        return true;
    }

    private void unloadWorld(CommandSender sender, String worldName) {
        if (!GWorld.getInstance().getDataHandler().containsWorld(worldName)) {
            sender.sendMessage(SendMessageUtil.sendMessage("Unload.failed").replaceAll("%world%", worldName));
            return;
        }

        ManageableWorld mWorld = GWorld.getInstance().getWorldManager().getWorld(worldName);

        if(!mWorld.isMapLoaded()) {
            sender.sendMessage(SendMessageUtil.sendMessage("Unload.alreadyUnloaded").replaceAll("%world%", worldName));
            return;
        }

        sender.sendMessage(SendMessageUtil.sendMessage("Unload.unloading").replaceAll("%world%", worldName));

        World mainWorld = Bukkit.getWorld(GWorld.getInstance().getConfig().getString("MainWorld"));
        for(Player player : mainWorld.getPlayers()) {
            player.teleport(mainWorld.getSpawnLocation());
            player.sendMessage(SendMessageUtil.sendMessage("Unload.teleport_players").replaceAll("%world%", worldName));
        }

        if (mWorld.unloadMap()) {
            sender.sendMessage(SendMessageUtil.sendMessage("Unload.success").replaceAll("%world%", worldName));
            return;
        }

        sender.sendMessage(SendMessageUtil.sendMessage("Unload.failed").replaceAll("%world%", worldName));
    }

}
