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

        GWorld.getInstance().getWorldManager().getWorld(player.getWorld().getName()).ifPresent(manageableWorld -> {
            if(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, manageableWorld)) {
                player.setGameMode(WorldProperty.getValue(WorldProperty.GAMEMODE, manageableWorld));
            }
        });

        // Update notification
        //todo
    }

}
