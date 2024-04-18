package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityUtil {

    public static void getAllEntities() {
        for(EntityType entityType : EntityType.values()) {
            if(!entityType.isAlive()) {
                continue;
            }

            Class<? extends Entity> entity = entityType.getEntityClass();

            assert entity != null;

            if(Enemy.class.isAssignableFrom(entity)) {
                GWorld.MONSTER.add(entity.getSimpleName());
            } else {
                GWorld.ANIMALS.add(entity.getSimpleName());
            }
        }
    }

}
