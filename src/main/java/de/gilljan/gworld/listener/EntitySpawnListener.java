package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.setCancelled(false);
            return;
        }

        World world = event.getLocation().getWorld();

        if(GWorld.getInstance().getWorldManager().getWorld(world.getName()) != null) {
            event.setCancelled(false);
            return;
        }

        WorldData worldData = GWorld.getInstance().getDataHandler().getWorld(world.getName());

        if(WorldProperty.getValue(WorldProperty.ANIMAL_SPAWNING, worldData)) {
            if(GWorld.ANIMALS.contains(event.getEntity().getClass().getSimpleName())) {
                event.setCancelled(true);
                return;
            }
        }

        if(WorldProperty.getValue(WorldProperty.MONSTER_SPAWNING, worldData)) {
            if(GWorld.MONSTER.contains(event.getEntity().getClass().getSimpleName())) {
                event.setCancelled(true);
                return;
            }
        }

        if(WorldProperty.getValue(WorldProperty.DISABLED_ANIMALS, worldData).contains(event.getEntity().getClass().getSimpleName())
                || WorldProperty.getValue(WorldProperty.DISABLED_MONSTERS, worldData).contains(event.getEntity().getClass().getSimpleName())) {
            event.setCancelled(true);
        }
    }

}
