package de.gilljan.gworld.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GSetCommand extends ArgsCommand{
    public GSetCommand() {
        super("gset", 2, "Set.use", "gworld.commands.gset");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        return false;
    }
}
