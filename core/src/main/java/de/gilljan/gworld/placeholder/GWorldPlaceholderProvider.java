package de.gilljan.gworld.placeholder;

import org.bukkit.OfflinePlayer;

public interface GWorldPlaceholderProvider {
    boolean matches(String placeholder);

    String resolve(OfflinePlayer player, String placeholder);
}
