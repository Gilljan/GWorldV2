package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();

        if(GWorld.getInstance().getDataHandler().containsWorld(p.getWorld().getName())) {
            // Get the world data
            // Set the player's gamemode to the world's default gamemode
            if(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, GWorld.getInstance().getDataHandler().getWorld(p.getWorld().getName()))) {
                p.setGameMode(WorldProperty.getValue(WorldProperty.GAMEMODE, GWorld.getInstance().getDataHandler().getWorld(p.getWorld().getName())));
            }
        }
    }
}
