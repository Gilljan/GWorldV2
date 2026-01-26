package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        String worldName = p.getWorld().getName();

        GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresent(manageableWorld -> {
            if(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, manageableWorld)) {
                p.setGameMode(WorldProperty.getValue(WorldProperty.GAMEMODE, manageableWorld));
            }
        });
    }
}
