package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.enums.WorldTypeMapping;
import de.gilljan.gworld.utils.SecureWorldNameUtil;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GImportCommand extends ArgsCommand {

    public GImportCommand() {
        super("gimport", 2, "Import.use", "gworld.commands.gimport");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        String worldName = args[0];
        String worldType = args[1];
        String generator = null;

        if (!SecureWorldNameUtil.isSecuredWorldName(worldName)) {
            player.sendMessage(SendMessageUtil.sendMessage("SecurityMessage"));
            return false;
        }

        if(GWorld.getInstance().getDataHandler().containsWorld(worldName)) {
            player.sendMessage(SendMessageUtil.sendMessage("Import.failed").replaceAll("%world%", worldName));
            return false;
        }

        if(args.length == 3) {
            generator = args[2];
        }

        WorldTypeMapping mapping = WorldTypeMapping.fromString(worldType);

        if(mapping == null) {
            player.sendMessage(SendMessageUtil.sendMessage("Import.failed"));
            return false;
        }

        WorldData worldData = new WorldData(worldName, mapping.getEnvironment(), mapping.getWorldType(),0, generator);

        player.sendMessage(SendMessageUtil.sendMessage("Import.creating").replaceAll("%world%", worldName));

        ManageableWorld mWorld = GWorld.getInstance().getWorldManager().addWorld(worldData);

        if(!mWorld.importExisting()) {
            player.sendMessage(SendMessageUtil.sendMessage("Import.failed").replaceAll("%world%", worldName));
            return false;
        }

        player.sendMessage(SendMessageUtil.sendMessage("Import.success").replaceAll("%world%", worldName));

        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        return false;
    }
}
