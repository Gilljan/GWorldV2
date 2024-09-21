package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GRecreateCommand extends ArgsCommand {

    public GRecreateCommand() {
        super("grecreate", 2, "Create.use", "gworld.commands.grecreate");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        String worldName = args[0];
        boolean saveOldWorld = Boolean.parseBoolean(args[1]);

        ManageableWorld world = GWorld.getInstance().getWorldManager().getWorld(worldName);

        reCreateWorld(player, world, saveOldWorld);

        return false;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        String worldName = args[0];
        boolean saveOldWorld = Boolean.parseBoolean(args[1]);

        ManageableWorld world = GWorld.getInstance().getWorldManager().getWorld(worldName);

        reCreateWorld(console, world, saveOldWorld);

        return false;
    }

    private void reCreateWorld(CommandSender sender, ManageableWorld world, boolean saveOldWorld) {
        if(world == null) {
            sender.sendMessage(SendMessageUtil.sendMessage("ReCreate.failed").replaceAll("%world%", "worldNotFound"));
            return;
        }

        if(!world.isMapLoaded()) {
            sender.sendMessage(SendMessageUtil.sendMessage("ReCreate.failed").replaceAll("%world%", world.getWorldName()));
            return;
        }

        if(world.reCreate(saveOldWorld)) {
            sender.sendMessage(SendMessageUtil.sendMessage("ReCreate.success").replaceAll("%world%", world.getWorldName()));
        } else {
            sender.sendMessage(SendMessageUtil.sendMessage("ReCreate.failed").replaceAll("%world%", world.getWorldName()));
        }
    }
}
