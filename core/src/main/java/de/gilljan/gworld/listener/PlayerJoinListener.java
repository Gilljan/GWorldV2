package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GWorld.getInstance().getDataHandler().getWorld(player.getWorld().getName()).ifPresent(worldData -> {
            if(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, worldData)) {
                player.setGameMode(WorldProperty.getValue(WorldProperty.GAMEMODE, worldData));
            }
        });

        // Update notification
        //todo
    }

}
