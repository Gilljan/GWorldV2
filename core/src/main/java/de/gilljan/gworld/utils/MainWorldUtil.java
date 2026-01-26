package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;
import org.bukkit.Bukkit;

public class MainWorldUtil {

    private MainWorldUtil(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isMainWorld(String worldName) {
        String mainWorldName = GWorld.getInstance().getConfig().getString("MainWorld");

        if(mainWorldName == null || mainWorldName.isEmpty()) {
            mainWorldName = Bukkit.getWorlds().get(0).getName();
        }

        return worldName.equalsIgnoreCase(mainWorldName);
    }
}
