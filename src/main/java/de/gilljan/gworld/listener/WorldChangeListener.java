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
        String worldName = p.getWorld().getName();

        GWorld.getInstance().getDataHandler().getWorld(worldName).ifPresent(worldData -> {
            if(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, worldData)) {
                p.setGameMode(WorldProperty.getValue(WorldProperty.GAMEMODE, worldData));
            }
        });
    }
}
