package de.gilljan.gworld.utils;

import org.bukkit.command.CommandSender;

public class SecureWorldNameUtil {

    public static boolean isSecuredWorldName(String worldName) {
        return worldName.contains(".") || worldName.contains("/") || worldName.equalsIgnoreCase("plugins") || worldName.equalsIgnoreCase("logs") || worldName.equalsIgnoreCase("old_maps");
    }

}
