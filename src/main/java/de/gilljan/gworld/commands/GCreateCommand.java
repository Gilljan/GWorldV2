package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.enums.WorldTypeMapping;
import de.gilljan.gworld.utils.SecureWorldNameUtil;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class GCreateCommand extends ArgsCommand {
    private static final Random RANDOM = new Random();

    public GCreateCommand() {
        super("gcreate", 2, "Create.use", "gworld.commands.gcreate");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        createWorld(player, args);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        createWorld(console, args);
        return true;
    }

    private void createWorld(CommandSender sender, String[] args) {
        String worldName = args[0];
        String worldType = args[1];
        String worldSeed = null;
        String generator = null;

        if (SecureWorldNameUtil.isSecuredWorldName(worldName)) {
            sender.sendMessage(SendMessageUtil.sendMessage("SecurityMessage"));
            return;
        }

        if(GWorld.getInstance().getDataHandler().containsWorld(worldName)) {
            sender.sendMessage(SendMessageUtil.sendMessage("Create.failed").replaceAll("%world%", worldName));
            return;
        }


        if(args.length == 3) {
            if(Character.isDigit(args[2].charAt(0))) {
                worldSeed = args[2];
            } else {
                generator = args[2];
            }
        }

        if(args.length == 4) {
            worldSeed = args[2];
            generator = args[3];
        }

        WorldTypeMapping mapping = WorldTypeMapping.fromString(worldType);

        if(mapping == null) {
            sender.sendMessage(SendMessageUtil.sendMessage("Create.use"));
            return;
        }

        WorldData world = null;
        try {
            world = new WorldData(worldName, mapping.getEnvironment(), mapping.getWorldType(), worldSeed == null ?  RANDOM.nextLong() : Long.parseLong(worldSeed), generator);
        } catch (NumberFormatException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Create.use"));
        }

        if(world == null) {
            sender.sendMessage(SendMessageUtil.sendMessage("Create.failed").replaceAll("%world%", worldName));
        }

        ManageableWorld manageableWorld = GWorld.getInstance().getWorldManager().addWorld(world);

        sender.sendMessage(SendMessageUtil.sendMessage("Create.creating").replaceAll("%world%", worldName));

        manageableWorld.createMap();

        sender.sendMessage(SendMessageUtil.sendMessage("Create.success").replaceAll("%world%", worldName));
    }
}
