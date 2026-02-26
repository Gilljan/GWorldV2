package de.gilljan.gworld.placeholder.provider;

import de.gilljan.gworld.api.IManageableWorld;
import de.gilljan.gworld.api.IWorldManager;
import de.gilljan.gworld.placeholder.GWorldPlaceholderProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class WorldNameProvider implements GWorldPlaceholderProvider {

    private final IWorldManager worldManager;

    public WorldNameProvider(IWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public boolean matches(String placeholder) {
        return placeholder.equalsIgnoreCase("name") || placeholder.startsWith("name_");
    }

    @Override
    public String resolve(OfflinePlayer player, String placeholder) {
        // %gworld_name% (current world)
        if (placeholder.equalsIgnoreCase("name")) {
            if (player instanceof Player onlinePlayer) {
                String currentWorld = onlinePlayer.getWorld().getName();
                return this.worldManager.getWorld(currentWorld)
                        .map(IManageableWorld::getWorldName)
                        .orElse("");
            }
            return "";
        }

        //  %gworld_name_<worldname>%
        String targetWorld = placeholder.substring(5);
        return this.worldManager.getWorld(targetWorld)
                .map(IManageableWorld::getWorldName)
                .orElse("");
    }
}
