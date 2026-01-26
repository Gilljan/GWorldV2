package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
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

        if(GWorld.getInstance().getWorldManager().getWorld(world.getName()).isEmpty()) {
            event.setCancelled(false);
            return;
        }

        ManageableWorld manageableWorld = GWorld.getInstance().getWorldManager().getWorld(world.getName()).get();

        EntityType spawnedType = event.getEntity().getType();
        String spawnedTypeName = spawnedType.name().toLowerCase();

        if(!WorldProperty.getValue(WorldProperty.ANIMAL_SPAWNING, manageableWorld)) {
            if(GWorld.ANIMALS.contains(spawnedTypeName)) {
                event.setCancelled(true);
                return;
            }
        }

        if(!WorldProperty.getValue(WorldProperty.MONSTER_SPAWNING, manageableWorld)) {
            if(GWorld.MONSTER.contains(spawnedTypeName)) {
                event.setCancelled(true);
                return;
            }
        }

        if(WorldProperty.getValue(WorldProperty.DISABLED_ANIMALS, manageableWorld).contains(spawnedTypeName)
                || WorldProperty.getValue(WorldProperty.DISABLED_MONSTERS, manageableWorld).contains(spawnedTypeName)) {
            event.setCancelled(true);
            return;
        }
    }

}
