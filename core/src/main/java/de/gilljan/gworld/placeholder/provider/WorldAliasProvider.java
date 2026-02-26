package de.gilljan.gworld.placeholder.provider;

import de.gilljan.gworld.api.IManageableWorld;
import de.gilljan.gworld.api.IWorldManager;
import de.gilljan.gworld.placeholder.GWorldPlaceholderProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class WorldAliasProvider implements GWorldPlaceholderProvider {

    private final IWorldManager worldManager;

    public WorldAliasProvider(IWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("alias") || input.startsWith("alias_");
    }

    @Override
    public String resolve(OfflinePlayer player, String input) {
        // %gworld_alias% (current world)
        if (input.equalsIgnoreCase("alias")) {
            if (player instanceof Player onlinePlayer) {
                String currentWorld = onlinePlayer.getWorld().getName();
                return getAliasOrName(currentWorld);
            }
            return "";
        }

        //  %gworld_alias_<worldname>%
        String targetWorld = input.substring(6);
        return getAliasOrName(targetWorld);
    }

    private String getAliasOrName(String name) {
        return worldManager.getWorld(name)
                .map(IManageableWorld::getDisplayName)
                .orElse("");
    }
}
