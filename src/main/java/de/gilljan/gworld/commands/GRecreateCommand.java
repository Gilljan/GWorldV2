package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.utils.SendMessageUtil;
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

    private void reCreateWorld(CommandSender sender, String worldName, boolean saveOldWorld) {
        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresentOrElse(
                world -> {
                    if (!world.isMapLoaded()) {
                        sender.sendMessage(SendMessageUtil.sendMessage("ReCreate.failed").replace("%world%", worldName));
                        return;
                    }

                    String messageKey = world.reCreate(saveOldWorld) ? "ReCreate.success" : "ReCreate.failed";
                    sender.sendMessage(SendMessageUtil.sendMessage(messageKey).replace("%world%", world.getWorldName()));
                },
                () -> sender.sendMessage(SendMessageUtil.sendMessage("ReCreate.failed").replace("%world%", worldName))
        );
    }
}
