package de.gilljan.gworld.utils;

import org.bukkit.command.CommandSender;

public class SecureWorldNameUtil {

    public static boolean isSecuredWorldName(String worldName) {
        return !worldName.matches("(?i)(.*\\..*|.*/.*|.*plugins.*|.*logs.*|.*old_maps*.)");
    }

}
